# -*-coding:utf-8
import platform
import subprocess
import urllib
from redminelib import Redmine
from datetime import datetime
from pathlib import Path
from pandas import Series,DataFrame


def check_log(start_name, creat_bug, project_issue,assigned_to):
    logpath = Path.cwd().parent
    log_server_url = logpath / 'log' / 'MTBF-log'
    aee_pasre = 'aee_parse_linux' if platform.system().__eq__(
        'Linux') else 'aee_parse_win.exe'
    aee_parse_url = logpath / 'aeeParse' / aee_pasre
    print(aee_parse_url)
    iscreat = creat_bug == 'true' and check_url('http://192.168.3.75:8006/redmine')
    for _ in log_server_url.iterdir():
        # print(type(_))
        print(datetime.strptime(_.name, "%Y%m%d_%H%M%S"))
        if datetime.strptime(_.name, "%Y%m%d_%H%M%S") >= start_name:
            aee_dict = {}
            aee_type = []
            aee_parant_path = []
            aee_info = []
            aee_err_package = []
            aee_err_time = []
            aee_parant = []
            for dbg_file in _.glob(r'**/*.dbg'):
                print(dbg_file)
                # Thread(target=parse_log,args=(aee_parse_url,dbg_file,)).start()
                parse_log(aee_parse_url, dbg_file)
                # for zz_internal in _.glob(r'**/ZZ_INTERNAL'):
                for zz_internal in dbg_file.parent.glob(r'ZZ_INTERNAL*'):
                    aee_parant.append(dbg_file.relative_to(logpath / 'log'))
                    with zz_internal.open(mode='r') as f:
                        result = f.read()
                        tmp_list = result.split(',')
                        if tmp_list[0] in aee_dict.keys():
                            aee_dict[tmp_list[0]] += 1
                        else:
                            aee_dict[tmp_list[0]] = 1
                        aee_type.append(tmp_list[0])
                        aee_parant_path.append(tmp_list[4])
                        aee_info.append(tmp_list[6])
                        aee_err_package.append(tmp_list[7])
                        aee_err_time.append(tmp_list[8])
            print(len(aee_dict))
            if len(aee_dict):
                description = ''
                subject = '[bug](monkey test)Total Error:  {};'.format(len(aee_type))
                for k, v in aee_dict.items():
                    subject = subject + '{0}:  {1} times;'.format(k, v)

                for index in range(len(aee_type)):
                    print(index)
                    description = description + ('\r\n{0} {1}:{2} in the module {3} at {4},\
                    log path:{5} \n AEE file path:{6}'.format(index,
                                                              aee_type[index],
                                                              aee_info[index],
                                                              aee_err_package[index],
                                                              aee_err_time[index],
                                                              aee_parant_path[index],
                                                              aee_parant[index]))
                if iscreat:
                    monkey_bug = CreatBug()
                    monkey_bug.set_project_id(project_issue)
                    monkey_bug.set_parameter(subject=subject, description=description,
                                             assigned=assigned_to)
                    monkey_bug.creat_isses()
                print(description)


def parse_log(aee_parse_url, dbg_file):
    subprocess.Popen('{exec_aee} {arg}'.format(exec_aee=aee_parse_url, arg=dbg_file), stdin=subprocess.PIPE,
                     stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True)


def check_url(test_url):
    opener = urllib.request.build_opener()
    opener.addheaders = []
    # test_url = 'http://192.168.3.75:8006/redmine'
    try:
        opener.open(test_url, timeout=3)
        print('Network connection available:{}'.format(test_url))
        return True
    except urllib.error.HTTPError:
        print('无法访问{}'.format(test_url))
        return False
    except urllib.error.URLError:
        print('无法访问{}'.format(test_url))
        return False
    except Exception as err:
        print(err)
        return False


class CreatBug(object):
    _name_to_id_dict = {
        "liucheng": "699",
        "zhanghuiliang": "737",
        "liuwenhua": "811"}
    _priority_dict = {
        'Low': 1,
        'Normal': 2,
        'High': 3,
        'Urgent': 4}
    _tracker_dict = {
        'Project': 2,
        'Upperstream': 3,
        'Mechanical': 4,
        'Hardware': 5,
        'Software': 6,
        'Production': 8,
        'Quality': 7,
        'Aftersales': 9,
        'Product': 17,
        'UX': 18,
    }
    _bug_level_dict = {
        'level A': 'A (FMEA得分 >1000)',
        'level B': 'B (1000>= FMEA得分 >600)',
        'level C': 'C (600 >= FMEA得分 >300)',
        'level D': 'D (300 >= FMEA得分 >100)',
        'level E': 'E (100 >= FMEA得分)'
    }

    def __init__(self,
                 username='SRtest',
                 password='123456',
                 redmine_url='http://192.168.3.75:8006/redmine'
                 ):
        self.redmine = Redmine(
            url=redmine_url,
            username=username,
            password=password,
            requests={'verify': False})
        self.project_id = 1434
        self.subject = '[bug](monkey)monkey test'
        self.description = 'no description'
        self.bug_level = 'level B'
        self.tracker_id = 6
        self.priority_id = 2  # 优先级 Normal
        self.assigned_to_id = 906

    def set_parameter(self, subject, description, assigned, tracker='Software', priority='Normal', bug_level='level B'):
        # self.project_id = self._project_dict[project]
        self.priority_id = self._priority_dict[priority]
        self.assigned_to_id = assigned
        self.subject = subject
        self.description = description
        self.tracker_id = self._tracker_dict[tracker]
        self.bug_level = bug_level

    def creat_isses(self):
        self.redmine.issue.create(
            project_id=self.project_id,
            subject=self.subject,
            description=self.description,
            tracker_id=self.tracker_id,
            priority_id=self.priority_id,

            assigned_to_id=self.assigned_to_id,
            custom_fields=[
                # {'id': 15, 'name': 'BUG缺陷等级', 'value': '5--轻微缺陷（Enhancement）'},
                # {'id': 16, 'name': 'BUG可见性', 'value': '10--必现(Always)'},
                # {'id': 17, 'name': 'BUG复现概率', 'value': '10--必现(Always)'},
                {'id': 20, 'name': 'BUG等级', 'value': self._bug_level_dict[self.bug_level]},
                # {'name': 'BUG_FMEA得分（BUG缺陷等级x复现概率x可见性）', 'value': '', 'id': 19}
                # {'name': 'BUG等级', 'id': 20, 'value': 'C (600 >= FMEA得分 >300)'}
                # {'id': 19, 'name': 'BUG_FMEA得分（BUG缺陷等级x复现概率x可见性）', 'value': '500', },
                {'id': 21, 'name': '问题或任务类别', 'value': '问题反馈Issue'},
                {'id': 25, 'name': '软件平台', 'value': 'Android8.0'},
                {'id': 26, 'name': '问题涉及模块', 'multiple': True, 'value': ['Monkey test']},
                {'id': 27, 'name': ' 问题修改涉及范围', 'value': '本项目'}]
            # {'id': 38, 'name': 'BUG难易度', 'value': '1'}]
        )

    def set_project_id(self, project_id):
        issue = self.redmine.issue.get(int(project_id))
        self.project_id = issue.project.id
