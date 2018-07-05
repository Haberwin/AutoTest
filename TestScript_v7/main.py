import configparser
from datetime import datetime
import logging
import os
import re
import subprocess
import sys
import traceback
import unittest
from random import random
from time import  ctime

import uiautomator2
from PyQt5 import QtCore, QtWidgets, QtGui
from PyQt5.QtCore import QThread, pyqtSignal, QBasicTimer
from PyQt5.QtWidgets import QApplication, QMainWindow, QProgressDialog

from Device import Testcase
from HTMLTestRunner_PY3 import HTMLTestRunner
from Testcase import Camera
from ValStressTest import Ui_MainWindow

logger = logging.getLogger("Testlog")


class MyWindow(Ui_MainWindow):
    VersionDict = {
        "Android 7": 7.0,
        "Android 8": 8.0
    }

    def __init__(self):
        super(MyWindow, self).__init__()
        self.MainWindow = QMainWindow()
        self.setupUi(self.MainWindow)

        # self.Button_start.clicked.connect(self.Button_stop.click)
        self.Button_start.clicked.connect(self.runtest)
        self.Button_start.clicked.connect(self.Step_Log.clear)
        #self.Step_Log.textChanged.connect(self.StepScroll)
        self.Button_stop.clicked.connect(self.end)
        self.config_file = 'Testcase/config.ini'
        self.cp = configparser.ConfigParser()
        self.cp.read(self.config_file)
        self.logthread = LogcatThread()
        self.logthread.sinOut.connect(self.Logcat.append)
        self.logthread.sinOutClear.connect(self.Logcat.clear)
        self.ThreadStress = TestTableThread()
        self.checkDevice = CheckDevice()
        self.checkDevice.FindDevice.connect(self.logthread.start)
        self.checkDevice.FindDevice.connect(self.ThreadStress.start)
        self.font = QtGui.QFont()
        self.font.setFamily("Microsoft YaHei")

        self.get_value()

    def end(self):
        self.logthread.terminate()
        self.ThreadStress.terminate()

    def get_value(self):
        self.treeWidget.setSortingEnabled(False)

        for session in self.cp.sections():
            topitem = QtWidgets.QTreeWidgetItem(self.treeWidget)
            if str(session) != "Initialize settings":
                topitem.setCheckState(0, QtCore.Qt.Unchecked)

            # topitem.setCheckState(0, QtCore.Qt.Checked)
            topitem.setText(0, str(session))
            for item in self.cp.options(session):
                seconditem = QtWidgets.QTreeWidgetItem(topitem)
                # seconditem.setCheckState(0, QtCore.Qt.Checked)
                seconditem.setText(0, str(item))
                seconditem.setFlags(QtCore.Qt.ItemIsEditable | QtCore.Qt.ItemIsEnabled | QtCore.Qt.ItemIsSelectable)
                seconditem.setText(1, str(self.cp.get(session, item)))

    def set_value(self):
        for treeitem in range(0, self.treeWidget.topLevelItemCount()):
            for childitem in range(0, self.treeWidget.topLevelItem(treeitem).childCount()):
                self.cp.set(self.treeWidget.topLevelItem(treeitem).text(0),
                            self.treeWidget.topLevelItem(treeitem).child(childitem).text(0),
                            self.treeWidget.topLevelItem(treeitem).child(childitem).text(1))
        self.cp.write(open(self.config_file, 'w'))

    def runtest(self):
        try:
            self.set_value()
            logger.info(str(ctime()))
            newDir = "testresult/log-" + datetime.now().strftime("%Y-%m-%d-%H-%M-%S")
            os.mkdir(newDir)
            QThread.sleep(1)
            self.ThreadStress.reportfile = newDir + '/Report.html'
            self.logthread.reportdir = newDir
            # Testcase.LOG_Switch = True
            self.ThreadStress.treeWidget = self.treeWidget
            self.checkProcess = QProgressDialog()
            self.checkProcess.setFont(self.font)
            
            self.checkProcess.setCancelButtonText("Close")
            self.checkProcess.setWindowTitle("Device checking...")
            
            self.checkDevice.Process.connect(self.checkProcess.setValue)
            self.checkDevice.FindDevice.connect(self.checkProcess.close)
            self.checkProcess.resize(400, 150)
            self.checkDevice.start()
            self.checkProcess.exec_()

            # self.TestTable.sinOutLog.connect(self.exportlog)
            # self.TestTable.sinOutProcess.connect(self.set_BarProcess)

        except Exception as err:
            # logger.error(str(err))
            traceback.print_exc()


    def set_BarProcess(self, process):
        self.progressBar.setProperty("value", process)

    def StepScroll(self):
        self.Step_Log.scroll(self.Step_Log.maximumWidth(),self.Step_Log.maximumHeight())



class CheckDevice(QThread):
    FindDevice = pyqtSignal()
    Process=pyqtSignal(int)

    def run(self):
        self.Process.emit(0)
        try:
            adr = os.popen('adb devices').read()
            devices = re.findall(r'(.*?)\s+device', adr)
            if len(devices) > 1:
                initlog = subprocess.Popen('python -m uiautomator2 init', shell=True, stdout=subprocess.PIPE,
                                           stderr=subprocess.PIPE)

                _ = 5
                while True:
                    _ += round(random()*3)
                    QThread.sleep(random()*3)
                    if _ > 90:
                        _=90
                        break
                    self.Process.emit(_)
                    if initlog.poll()==0:
                        break
                self.Process.emit(_)
                initlog.wait()
                self.Process.emit(95)
                deviceIds = devices[1:]
                for device in deviceIds:
                    d = uiautomator2.connect_usb(device)
                    connect_flag = True
                    while connect_flag is not None:
                        connect_flag = d.unlock()
                        QThread.sleep(2)
                        if connect_flag is None:
                            logger.info('Connected: ' + device)
                            logger.info(d.device_info)
            else:
                logger.error('No device found!!!!')
        except Exception as err:
            logger.error(err)
        finally:
            self.Process.emit(100)
            self.FindDevice.emit()


class TestTableThread(QThread):
    sinOutLog = pyqtSignal(str)
    sinOutProcess = pyqtSignal(int)
    DICT3 = {
        'IOCamera': Camera.IOCamera,
        'ShootingStress': Camera.ShootingStress,
        'SwitchCamera': Camera.SwitchCamera,
        'SwitchRecord': Camera.SwitchRecord,
        'RecordStress': Camera.RecordStress
    }

    def __init__(self):
        super(TestTableThread, self).__init__()
        self.__report_file = 'testresult/Report.html'
        self.__treeWidget = None

    @property
    def treeWidget(self):
        return self.__treeWidget

    @treeWidget.setter
    def treeWidget(self, treewidget):
        self.__treeWidget = treewidget

    @property
    def reportfile(self):
        return self.__report_file

    @reportfile.setter
    def reportfile(self, file):
        self.__report_file = file

    def run(self):
        report_title = 'Test Result'
        desc = '测试结果：'

        try:
            logger.info("start")
            testsuite = unittest.TestSuite()
            for treeitem in range(self.__treeWidget.topLevelItemCount()):
                if self.__treeWidget.topLevelItem(treeitem).checkState(0) == 2:
                    testitem = self.__treeWidget.topLevelItem(treeitem).text(0)
                    logger.info(testitem)
                    CurrentTest = self.DICT3[testitem]
                    testsuite.addTest(unittest.TestLoader().loadTestsFromTestCase(CurrentTest))
            with open(self.__report_file, 'wb') as report:
                runner = HTMLTestRunner(stream=report, title=report_title, description=desc)
                runner.run(testsuite)
            # Testcase.LOG_Switch = False
            logger.info("end")
        except Exception as e:
            logger.error(e)
            traceback.print_exc()

    def return_process(self, cycle):
        if self.cycletimes is not "":
            process = (self.item + float(cycle) / float(self.cycletimes)) / self.rowCount * 100
            self.sinOutLog.emit(str(process))
        else:
            process = float(self.item + 1) / self.rowCount * 100
        self.sinOutProcess.emit(process)
class LogcatThread(QThread):
    sinOut = pyqtSignal(str)
    sinOutClear = pyqtSignal()
    def __init__(self):
        super(LogcatThread, self).__init__()
        self.__LIST_MATCH = "ANR in|FATAL"
        self.__reportdir = 'testresult'
        # self.log_file = open(Testcase.FILE_PATH + '/logreport1.txt', 'w')
    @property
    def reportdir(self):
        return self.__reportdir

    @reportdir.setter
    def reportdir(self, file):
        self.__reportdir = file

    @property
    def serial(self):
        return self.__serial

    @serial.setter
    def serial(self, serialno):
        self.__serial = serialno

    def run(self):
        self.__LIST_MATCH = self.get_item('Initialize settings', 'error match')
        self.log_data = subprocess.Popen('adb logcat', shell=True, stdout=subprocess.PIPE,
                                         stderr=subprocess.PIPE)  # log_data recieves
        self.d = uiautomator2.connect_usb(self.get_item('Initialize settings', 'serial'))
        subprocess.Popen("adb logcat -c", shell=True)
        logfilenum = 1
        error_num = 0
        datasize = 0
        cycle_num = 0
        reboot = False
        thread_bugreport = None
        error_log_file = None
        # Testcase.LogcatClose = False
        try:
            self.log_file = open('%s/logreport%d.txt' % (self.__reportdir, logfilenum), 'w')
        except Exception as er:
            logger.error(er)
            traceback.print_exc()
        # while self.get_item('Initialize settings', 'logcatrun') == 'True':
        while True:
            try:
                line = self.log_data.stdout.readline().decode('gbk', 'ignore')  # ,errors='ignore')# + '\r'
                self.sinOut.emit(line)
                for temp in range(0, 1000):
                    if len(line.strip()):
                        break
                else:
                    break
                mi = re.search(self.__LIST_MATCH, line)
                if mi is not None:
                    if cycle_num < 1:
                        error_num += 1
                    cycle_num = 40
                    errortype = mi.group()
                    filedir = '%s/%dth_error_data_%s' % (self.__reportdir, error_num, errortype)  # .replace(' ','_',10)
                    filedir = filedir.replace(' ', '_')
                    if not os.path.exists(filedir):
                        os.mkdir(filedir)
                        QThread.sleep(1)
                        error_log_file = open(filedir + '/error_log.txt', 'a')
                        error_log_file.write(line)

                        # Export screenshot picture
                        self.d.screenshot(filedir + '/screenshot.png')
                        # Export cpu information
                        subprocess.Popen('adb shell top -s cpu -m 10 -d 1 -n 5 >' + filedir + '/cpu.ini', shell=True)
                        # Export memory information
                        subprocess.Popen('adb shell dumpsys meminfo >' + filedir + '/memory.ini', shell=True)
                        # Export bugreport
                        if thread_bugreport is not None:
                            if not thread_bugreport.poll() is None:
                                thread_bugreport = subprocess.Popen('adb bugreport ' + filedir + '/bugreport',
                                                                    shell=True)
                    else:
                        if error_log_file:
                            error_log_file.write(line)
                else:
                    if cycle_num > 1:
                        error_log_file.write(line)
                    elif cycle_num == 1:
                        error_log_file.write(line)
                        error_log_file.close()
                    cycle_num -= 1

                # logger.info("writ5")
                self.log_file.write(line)
                datasize += len(line)
                # If the datasize of logcat data greater than 50Mb than export the logcat data to another file
                if datasize / 1024.0 / 1024.0 >= 50:
                    self.sinOutClear.emit()
                    self.sinOut.emit('---------------Switch log file,clear logbrowser!------------------\n')
                    self.log_file.close()
                    datasize = 0
                    logfilenum += 1
                    self.log_file = open
                    ('%s/logreport%d.txt' % (self.__reportdir, logfilenum), 'w')
                    # Testcase.Curren_Log_File = ('logreport' + str(logfilenum) + '.txt')
                # if self.get_item('Initialize settings', 'reboot') == 'True':
                if reboot:
                    logger.warning('reboot')
                    while not self.log_data.poll():
                        self.log_data.terminate()
                        self.log_data.kill()
                    self.log_file.close()
                    if thread_bugreport is not None:
                        thread_bugreport.wait()
                    # Testcase.LogcatClose = True
                    temp = 0
                    while self.get_item('Initialize settings', 'reboot') == 'True':
                        QThread.sleep(1)
                        temp += 1
                        if temp >= 120:
                            break
                        self.sinOut.emit("log wait......%ds......" % temp)
                    if temp < 120:
                        # Testcase.LogcatClose = False
                        self.log_file = open('%s/logreport%d.txt' % (self.__reportdir, logfilenum),
                                             'w')  # For saving the logdata
                        self.log_data = subprocess.Popen('adb logcat', bufsize=-1, shell=True, stdout=subprocess.PIPE,
                                                         stderr=subprocess.PIPE)  # self.log_data recieves
                        # Testcase.Curren_Log_File = ('logreport' + str(logfilenum) + '.txt')
                    else:
                        self.sinOut.emit('analyzed end!')
                        return
                        # sleep(0.01)
            except Exception as e:
                logger.error(e)
                traceback.print_exc()
        while not self.log_data.poll():
            try:
                self.log_data.terminate()
                self.log_data.kill()
            except Exception as e:
                logger.error(e)
                traceback.print_exc()

        self.log_file.close()
        if thread_bugreport is not None:
            thread_bugreport.wait()
            logger.info('Complete all!')

    def get_item(self, se, key):
        self.config = 'Testcase/config.ini'
        self.cfg = configparser.ConfigParser()
        self.cfg.read(self.config)
        value = self.cfg.get(se, key)
        return value

    def terminate(self):
        self.log_file.close()
        self.log_data.terminate()
        Testcase.LOG_Switch = False
        super().terminate()


class ConsoleWindowLogHandler(logging.Handler):
    def __init__(self, textBox):
        super(ConsoleWindowLogHandler, self).__init__()
        self.textBox = textBox
        formatter = logging.Formatter('%(asctime)s-%(filename)s[line:%(lineno)d]-%(levelname)s: %(message)s',
                                      datefmt='%D %H:%M:%S')
        self.setFormatter(formatter)
        self.setLevel(logging.INFO)

    def emit(self, logRecord):
        self.textBox.append(self.format(logRecord))


if __name__ == '__main__':
    app = QApplication(sys.argv)
    ui = MyWindow()
    consoleHandler = ConsoleWindowLogHandler(ui.Step_Log)
    logger.setLevel(logging.INFO)
    logger.addHandler(consoleHandler)
    ui.MainWindow.show()

    sys.exit(app.exec_())
