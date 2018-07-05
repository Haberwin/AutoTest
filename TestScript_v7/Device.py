import configparser
import uiautomator2
import unittest
import logging
from time import ctime

logger = logging.getLogger("Testlog")


class Testcase(unittest.TestCase):
    Curren_Log_File = ''
    Curren_Cycle_Num = 0
    DeviceReboot = False
    LogcatClose = False
    Android_version = 7.0
    LOG_Switch = True

    def __init__(self, *args):
        unittest.TestCase.__init__(self, *args)
        self.d = uiautomator2.connect_usb(self.get_item('Initialize settings', 'serial'))
        self.d.freeze_rotation()
        self.cycle = self.get_item(self.__class__.__name__, 'cycle number')

    def get_item(self, se, key):
        self.config = 'Testcase/config.ini'
        self.cfg = configparser.ConfigParser()
        self.cfg.read(self.config)
        value = self.cfg.get(se, key)
        return value


    def setUp(self):
        self.printlog('Start time:%s' % str(ctime()))
        self.passtimes = 0
        self.d.healthcheck()
        self.remove_app()

    def tearDown(self):
        self.printlog('Finish time:%s' % str(ctime()))

    def enter_app(self, App_Name):
        Apps = "Apps" if Testcase.Android_version == 7.0 else "Apps list"
        self.printlog('Enter ' + App_Name)
        self.d.press('home')

        if self.d(descriptionStartsWith=Apps).exists:
            self.d(descriptionStartsWith=Apps).click()

            self.d(scrollable=False).scroll.vert.to(description=App_Name)
            self.d(description=App_Name).click()

            return True
        else:
            self.printlog("Android version Error or Language is not English!")
            self.printlog("Please select the cerrect Android version and set the language to English")
            return False

    def remove_app(self):
        self.d.press('home')
        self.d.press('recent')
        self.d(resourceId='com.android.systemui:id/button_remove_all').click_exists(timeout=3)
        self.d.press('home')

    @staticmethod
    def printlog(log):
        logger.info(log)
        print(log)

    # @classmethod
    # def setUpClass(cls):
    #     cls.printlog('Start time:%s' % str(ctime()))
    #
    # @classmethod
    # def tearDownClass(cls):
    #
    #     cls.printlog('Finish time:%s' % str(ctime()))



    # def clear_app(self, App_Name):
    #     self.d.press('home')
    #
    #     self.d.press('recent')
    #
    #     self.printlog('Clear exits ' + App_Name)
    #     for j in range(0, 3):
    #         if self.d(description="Dismiss " + App_Name + ".").exists:
    #             self.d(description="Dismiss " + App_Name + ".").click()
    #             break
    #         sleep(1)
    #     self.d.press('home')

    # def reboot(self):
    #     self.printlog("Reboot start......")
    #     self.printlog(str(ctime()))
    #     Testcase.DeviceReboot = True
    #     for temp in range(0, 100):
    #         if Testcase.LogcatClose:
    #             break
    #         sleep(1)
    #     Testcase.LogcatClose = False
    #     subprocess.Popen('adb reboot', shell=True)
    #     sleep(5)
    #     temp = 0
    #     waitdevice = subprocess.Popen('adb wait-for-device', shell=True)
    #
    #     while waitdevice.poll() is None:
    #         self.printlog("device wait......" + str(temp) + "s......")
    #         sleep(1)
    #         temp += 1
    #     if not temp < 50:
    #         self.printlog("Canon find the adb device!")
    #         return False
    #     sleep(5)
    #     if not self.initialze_phone():
    #         self.printlog("Reboot Fail")
    #         Testcase.DeviceReboot = False
    #         return False
    #     Testcase.DeviceReboot = False
    #     self.printlog("Reboot success")
    #     self.printlog(str(ctime()))
    #     return True
