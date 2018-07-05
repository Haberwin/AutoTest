from datetime import datetime
import configparser
from time import sleep

from test import parse

config = configparser.ConfigParser()
config.read('setting.ini', encoding='utf-8-sig')
creat_bug = config.get('Redmine', 'creat bug')
project_issue = config.get('Redmine', 'project issue')
start_time = config.get('monkey', 'start time')
assigned_to = config.get('Redmine', 'assigned to id')
print(start_time)
parse.check_log(datetime.strptime(start_time, "%Y%m%d_%H%M%S"), creat_bug, project_issue, int(assigned_to))
print('Parse log succee!')
for _ in range(10):
    print('Process will exit after {} second!'.format(10 - _))
    sleep(1)

