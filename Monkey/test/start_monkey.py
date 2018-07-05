# -*-coding:utf-8

import subprocess
import os
import re
import configparser
from datetime import datetime
from threading import Thread
from pathlib import Path
from time import sleep

config = configparser.ConfigParser()
config.read('setting.ini', encoding='utf-8-sig')
logpath = Path.cwd().parent / 'log'
runpath = Path.cwd()
start_time = datetime.now()
test_time = re.findall(r'\d+', config.get('monkey', 'test time'))


def check_devices():
    devices = get_devices()
    if not devices:
        print('No devices found. Exit')
        return
    if len(devices):
        with open('setting.ini', 'w') as f:
            config.set('monkey', 'start time', value=start_time.strftime("%Y%m%d_%H%M%S"))
            config.write(f)

        print(devices)
        device = iter(devices)

        while True:
            try:
                run_monkey(next(device))

            except StopIteration:
                break
        thread_pull = pull_mtklog()
        os.chdir(str(runpath))
        try:
            while True:
                stout = thread_pull.stdout.readline().decode('utf-8', 'ignore')
                print(stout)
                if re.search(r'Sleep \d+s', stout):
                    print('Running time: {}'.format(datetime.now() - start_time))
                    if test_time and int(test_time[0]) * 3600 > int((datetime.now() - start_time).seconds):

                        asser_monkey_ps(True)
                        sleep(3)
                        continue
                    else:
                        break
        except KeyboardInterrupt:
            print('Monkey Interrupt because key abord')
        finally:
            asser_monkey_ps(False)
            print('Monkey test finished!')
            for _ in range(300):
                print('Process will exit after {} seconds!'.format(300 - _))
                sleep(1)
            thread_pull.terminate()
            print('Exit!')

    else:
        # print(pipe.stdout.read().decode('utf-8'))
        # print(pipe.stderr.read().decode('utf-8', 'ignore'))
        print('No device found! Abort test')


def get_devices():
    pipe = subprocess.Popen('adb devices', stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)

    if not pipe.stderr:
        print('No adb found,please install adb! Abort test')
        return False
    # print(pipe.stdout.read().decode('utf-8'))
    devices = re.findall(r'\s(\S+)\tdevice', pipe.stdout.read().decode('utf-8'))
    return devices


def run_monkey(serialno):
    monkey_command = config.get('monkey', 'command')
    print('adb -s {0} shell {1} '.format(serialno, monkey_command))
    thread_monkey = subprocess.Popen(
        'start /b adb -s {0} shell {1} > {2}/monkey-{0}-{3}.txt'.format(serialno, monkey_command, logpath / 'Monkey-log',
                                                                 datetime.now().strftime("%Y%m%d%H%M%S")),
        shell=True)


def asser_monkey_ps(donext=True):
    devices = get_devices()

    if not devices:
        print('No devices found!')
        return
    for serialno in devices:
        monkey_ps = subprocess.Popen('adb -s {0} shell ps | findstr monkey'.format(serialno), shell=True,
                                     stdout=subprocess.PIPE)
        IsMonkeyRun = False
        while True:
            ps_id = monkey_ps.stdout.readline()
            if ps_id != b'':
                IsMonkeyRun = True
                if donext:
                    print('Monkey still running in the devices {}'.format(serialno))
                else:
                    pid = re.findall(r'\d+', re.findall(r'shell\s+\d+', ps_id.decode('utf-8'))[0])[0]
                    subprocess.Popen('adb -s {0} shell kill {1}'.format(serialno, pid), shell=True,
                                     stdout=subprocess.PIPE)
            else:
                if donext and not IsMonkeyRun:
                    print('Restart monkey  in the devices {}'.format(serialno))
                    run_monkey(serialno)

                break


def pull_mtklog():
    log_exe = 'LoNg.jar'
    os.chdir(str(logpath))
    thread_pull_log = subprocess.Popen('java -jar {} start'.format(log_exe), shell=True, stdout=subprocess.PIPE,
                                       stderr=subprocess.PIPE)
    return thread_pull_log


if __name__ == '__main__':
    check_devices()
