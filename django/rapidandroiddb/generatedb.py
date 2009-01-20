from django.db import connection
from rapidandroid.models import *
import string

def run():
    from django.core.management import call_command 
    call_command('syncdb')
    creates = []
    for sqlhash in connection.queries:
        if string.count(sqlhash['sql'],"CREATE TABLE") == 1:
            creates.append(sqlhash['sql'])
    return creates
        



