#!/usr/bin/env python
# vim: noet

import random
from datetime import datetime, timedelta

# phone number data
COUNTRY_CODE = '+234'
AREA_CODE = '64'

# location data
LOCATION = {
	0: ['Kano', '364869', 2000],
	1: ['Bichi', '335274', 750],
	2: ['Karaye', '003745', 1250],
	3: ['Rano', '335465', 500],
	4: ['Gaya', '777364', 750],
	5: ['Ringim', '354385', 1500],
	6: ['Wudil', '625273', 1000]
} # location, local number, initial balance

TREND = {
	0: [500, 10, '--'],
	1: [500, 5, '-'],
	2: [500, 1, '0'],
}

# timespan data
WEEKS = 26

# form data (location, received, given, balance)
CODE = 'bednets'

# set up our time frame
dt = timedelta(weeks=WEEKS)
start = datetime.now() - dt

rawdata = open('rawdata.csv','w')
answerkey = open('answers.csv','w')


# loop through the time diff and make one entry per day
# rotating through the locations (i.e. one location per day)
count = 0
for day in range(dt.days):
	# what location/day are we at?
	index = day % len(LOCATION)

    # increment the time by one day
	# format: yyyy-MM-dd HH:mm:ss = "%Y-%m-%d %H:%M:%S"
	timestamp = (start + timedelta(days=day)).strftime("%Y-%m-%d %H:%M:%S")

	# set the phone and location info based on the index
	phone     = COUNTRY_CODE + AREA_CODE + LOCATION[index][1]
	code      = CODE
	location  = LOCATION[index][0]

	# pick a trend for this point
	trend = TREND[random.randint(0, max(range(len(TREND))))]

	# get the previous balance
	prev_balance = LOCATION[index][2]

	# get a random amount of new bednets (0 < received)
	received = random.randint(0, random.randint(0, trend[0]))

	# give out a random amount of bednets (0 <= given <= prev_balance + received
	given = random.randint(0, int((prev_balance + received) / trend[1]))
	if given < 0: given = 0

	# figure out the new balance and set it in the LOCATION
	balance = prev_balance + received - given
	LOCATION[index][2] = balance
        rawdata.write("%s,%s,%s %s %s %s %s\n" % \
		(timestamp, phone, code, location, received, given, balance))
        answerkey.write("%s, %s, %s, %s, %s\n" % \
                        (count, location, received, given, balance))
        
        count = count+1
rawdata.close()
answerkey.close()
##	# write the row data
##	print "%s,%s,%s %s %s %s %s" % \
##		(timestamp, phone, code, location, received, given, balance)
