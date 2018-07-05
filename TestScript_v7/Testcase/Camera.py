from time import sleep
from uiautomator2 import UiObjectNotFoundError
from urllib3.connectionpool import xrange
from Device import Testcase


class Camera(Testcase):
    def __init__(self, *args):
        Testcase.__init__(self, *args)

        self.Switch = {
            7.0: 'com.mediatek.camera:id/onscreen_camera_picker',
            8.0: 'com.mediatek.camera:id/camera_switcher'
        }
        self.Preview = {
            7.0: 'com.mediatek.camera:id/distance_info',
            8.0: 'com.mediatek.camera:id/focus_ring'
        }
        self.Shooting = {
            7.0: self.shooting7,
            8.0: self.shooting8
        }
        self.Record = {
            7.0: self.record7,
            8.0: self.record8
        }
        self.RecordStop = {
            7.0: self.stoprecord7,
            8.0: self.stoprecord8
        }
        self.ShutterInRecord = {
            7.0: self.shutterInRecord7,
            8.0: self.shutterInRecord8
        }

    def shooting7(self):
        self.d(resourceId='com.mediatek.camera:id/shutter_button_photo', description='Shutter button',
               clickable=True).click()

    def shooting8(self):
        self.d(resourceId='com.mediatek.camera:id/shutter_text', text='Picture', clickable=True).click()
        self.d(resourceId='com.mediatek.camera:id/shutter_image', description='Picture', clickable=True).click()

    def record7(self):
        self.d(resourceId='com.mediatek.camera:id/shutter_button_video', description='Video shutter button',
               clickable=True).click()

    def record8(self):
        self.d(resourceId='com.mediatek.camera:id/shutter_text', text='Video', clickable=True).click()
        self.d(resourceId='com.mediatek.camera:id/shutter_image', description='Video', clickable=True).click()

    def stoprecord7(self):
        self.d(resourceId='com.mediatek.camera:id/shutter_button_video', description='Video shutter button',
               clickable=True).click()

    def stoprecord8(self):
        self.d(resourceId='com.mediatek.camera:id/video_stop_shutter', clickable=True).click()

    def shutterInRecord7(self):
        self.d(resourceId='com.mediatek.camera:id/shutter_button_photo', description='Shutter button',
               clickable=True).click()

    def shutterInRecord8(self):
        self.d(resourceId='com.mediatek.camera:id/btn_vss', clickable=True).click()


class IOCamera(Camera):
    """Input and Exit Camera stress test"""

    # def __init__(self, *args):
    #     Camera.__init__(self, *args)
    #     self.cycle = self.get_item('IOCamera test', 'cycle number')

    def test_run(self):
        """Input and Exit Camera stress test"""

        for self.currentcycle in xrange(int(self.cycle)):
            try:
                with self.subTest(cycle=self.currentcycle + 1):
                    self.printlog('Start the %d time test!' % (self.currentcycle + 1))
                    self.remove_app()
                    self.enter_app("Camera")
                    sleep(2)
                    self.assertTrue(self.d(resourceId=self.Preview[self.Android_version]).exists,
                                    msg='Enter camera failure!')
                    self.printlog('Enter camera success')
                    self.d.press('back')
                    self.assertTrue(self.d(description="Camera").exists,
                                    msg='Exit camera failure!')
                    self.printlog('Exit camera success')
                    self.passtimes += 1
                    self.printlog('The %d time test Pass!' % (self.currentcycle + 1))
            except UiObjectNotFoundError as err:
                self.printlog('The %d time test failed!' % self.currentcycle + 1)
                self.fail('Reason:\tCan not find %s' % err.message)
            except Exception as err:
                self.fail("Unexcept Error:%s" % str(err))
        self.printlog('END Camera Test!')
        self.printlog('Pass %d times in %s times' % (self.passtimes, self.cycle))
        self.remove_app()


class ShootingStress(Camera):

    def test_1_shoot(self):
        self.assertTrue(self.enter_app("Camera"), msg='Cannot enter Camera')
        """Continue shoot"""
        for self.currentcycle in xrange(0, int(self.cycle)):
            with self.subTest(shootcycle=self.currentcycle + 1):
                try:
                    self.printlog('Start the %d time shooting.' % (self.currentcycle + 1))
                    self.Shooting[self.Android_version]()
                    self.passtimes += 1
                    self.printlog('The %d time test Pass!' % (self.currentcycle + 1))
                except UiObjectNotFoundError as err:
                    self.d.screenshot('screenshot.png')
                    self.printlog('The %d time test failed!' % (self.currentcycle + 1))
                    self.fail('Reason:\tCan not find %s' % err.message)
                except Exception as err:
                    self.fail("Unexcept Error:%s" % str(err))
        self.printlog('END Camera shooting Test!')
        self.printlog('Pass %d times in %s times' % (self.passtimes, self.cycle))

    def test_2_delete(self):
        """Delete photos"""
        self.passtimes = 0
        self.assertTrue(self.enter_app("Camera"), msg='Cannot enter Camera')
        self.d(resourceId='com.mediatek.camera:id/thumbnail', clickable=True).click()
        for self.currentcycle in xrange(0, int(self.cycle)):
            with self.subTest(deletecycle=self.currentcycle + 1):
                try:
                    self.printlog('Start the %d time delete.' % (self.currentcycle + 1))
                    self.d(description='More options').click()

                    self.d(resourceId='android:id/title', text='Delete').click()

                    self.d(className='android.widget.Button', text='OK').click()
                    self.passtimes += 1
                    self.printlog('The %d time test Pass!' % (self.currentcycle + 1))
                except UiObjectNotFoundError as err:
                    self.d.screenshot('screenshot.png')
                    self.printlog('The %d time test failed!' % (self.currentcycle + 1))
                    self.fail('Reason:\tCan not find %s' % err.message)
                except Exception as err:
                    self.fail("Unexcept Error:%s" % str(err))
        self.printlog('END Camera shooting Test!')
        self.printlog('Pass %d times in %s times' % (self.passtimes, self.cycle))
        self.remove_app()


class SwitchCamera(Camera):

    def test_switch(self):
        """Constant switching between main camera and front camera"""
        self.assertTrue(self.enter_app("Camera"), msg='Cannot enter Camera')
        for self.currentcycle in xrange(0, int(self.cycle)):
            try:
                with self.subTest(switch_cycle=self.currentcycle + 1):
                    self.printlog('Start the %d time shooting.' % (self.currentcycle + 1))
                    self.Shooting[self.Android_version]()
                    self.printlog('Main camera shoot success.')
                    self.d(resourceId=self.Switch[self.Android_version], clickable=True).click()
                    self.Shooting[self.Android_version]()
                    self.printlog('Front camera shoot success.')
                    self.d(resourceId=self.Switch[self.Android_version], clickable=True).click()
                    self.passtimes += 1
                    self.printlog('The %d time shooting success!' % (self.currentcycle + 1))
            except UiObjectNotFoundError as err:
                self.d.screenshot('screenshot.png')
                self.printlog('The %d time test failed!' % (self.currentcycle + 1))
                self.fail('Reason:\tCan not find %s' % err.message)
            except Exception as err:
                self.fail("Unexcept Error:%s" % str(err))
        self.printlog('END Switch Camera Test!')
        self.printlog('Pass %d times in %s times' % (self.passtimes, self.cycle))
        self.remove_app()


class SwitchRecord(Camera):

    def test_record_switch(self):
        """Switch between Record and camera"""
        self.assertTrue(self.enter_app("Camera"), msg='Cannot enter Camera')
        self.Record[self.Android_version]()
        for self.currentcycle in xrange(0, int(self.cycle)):
            with self.subTest(switch_cycle=self.currentcycle + 1):
                try:
                    self.printlog('Start the %d time test.' % (self.currentcycle + 1))
                    sleep(10)
                    self.ShutterInRecord[self.Android_version]()
                    self.printlog('Main camera shoot success.')
                    self.passtimes += 1
                    self.printlog('The %d time test pass!' % (self.currentcycle + 1))
                except UiObjectNotFoundError as err:
                    self.printlog(str(err))
                    self.d.screenshot('/screenshot.png')
                    self.printlog('The %d time test failed!' % (self.currentcycle + 1))
                    self.fail('Reason:\tCan not find %s' % err.message)
                except Exception as err:
                    self.fail("Unexcept Error:%s" % str(err))
        self.RecordStop[self.Android_version]()
        self.printlog('END Switch Record Test!')
        self.printlog('Pass %d times in %s times' % (self.passtimes, self.cycle))
        self.remove_app()


class RecordStress(Camera):

    def test_record_stress(self):
        """Record stress test"""
        self.assertTrue(self.enter_app("Camera"), msg='Cannot enter Camera')
        for self.currentcycle in xrange(0, int(self.cycle)):
            with self.subTest(recode_cycle=self.currentcycle + 1):
                try:
                    self.printlog('Start the %d time test.' % (self.currentcycle + 1))
                    self.Record[self.Android_version]()
                    sleep(60)
                    self.RecordStop[self.Android_version]()
                    self.printlog('Record success.')
                    self.passtimes += 1
                    self.printlog('The %d time test pass!' % (self.currentcycle + 1))
                except UiObjectNotFoundError as err:
                    self.printlog(str(err))
                    self.d.screenshot('/screenshot.png')
                    self.printlog('The %d time test failed!' % (self.currentcycle + 1))
                    self.fail('Reason:\tCan not find %s' % err.message)
                except Exception as err:
                    self.fail("Unexcept Error:%s" % str(err))
        self.printlog('END Record Stress Test!')
        self.printlog('Pass %d times in %s times' % (self.passtimes, self.cycle))

    def test_delete(self):
        """Delete record"""
        self.passtimes = 0
        self.assertTrue(self.enter_app("Camera"), msg='Cannot enter Camera')
        self.d(resourceId='com.mediatek.camera:id/thumbnail', clickable=True).click()
        for self.currentcycle in xrange(0, int(self.cycle)):
            with self.subTest(delete_cycle=self.currentcycle + 1):
                try:
                    self.printlog('Start the %d time delete.' % (self.currentcycle + 1))
                    self.d(description='More options').click()
                    self.d(resourceId='android:id/title', text='Delete').click()
                    self.d(className='android.widget.Button', text='OK').click()
                    self.passtimes += 1
                    self.printlog('The %d time test Pass!' % (self.currentcycle + 1))
                except UiObjectNotFoundError as err:
                    self.d.screenshot('screenshot.png')
                    self.printlog('The %d time test failed!' % (self.currentcycle + 1))
                    self.fail('Reason:\tCan not find %s' % err.message)
                except Exception as err:
                    self.fail("Unexcept Error:%s" % str(err))
        self.printlog('END Record Delete Test!')
        self.printlog('Pass %d times in %s times' % (self.passtimes, self.cycle))
        self.remove_app()
