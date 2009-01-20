from django.db import models

# Create your models here.

class Form(models.Model):
        formname = models.CharField(max_length=32, unique=True)
        prefix = models.CharField(max_length=16,unique=True)
        description = models.CharField(max_length=512)
        parsemethod = models.CharField(max_length=128)  #local db parser and/or

        def __unicode__(self):
                return self.formname
        
class FieldType(models.Model):
        name = models.CharField(max_length=32,unique=True)
        datatype = models.CharField(max_length=32) #primitive | Enum
        regex = models.CharField(max_length=1024)
        def __unicode__(self):
                return u"Type: %s (%s)" %(self.name,self.datatype)

class Field(models.Model):
        form = models.ForeignKey(Form)
        sequence = models.PositiveIntegerField("Sequence order in form")
        name = models.CharField(max_length=32,unique=True)
        prompt = models.CharField(max_length=64)
        fieldtype = models.ForeignKey(FieldType)
        def __unicode__(self):
                return u'%s [%s]' %(self.name,self.fieldtype)

##class EnumOption(models.Model):
##        fieldtype = models.ForeignKey(FieldType)
##        code = models.CharField(max_length=32)
##        real_value=models.CharField(max_length=64)
        


class Monitor(models.Model):
	first_name = models.CharField(max_length=50)
	last_name = models.CharField(max_length=50)
	alias = models.CharField(max_length=16, unique=True, help_text="Abbreviated name, lowercase letters")
	phone = models.CharField(max_length=30, blank=True, help_text="e.g., +251912555555")
	email = models.EmailField(blank=True)
	incoming_messages = models.PositiveIntegerField(help_text="The number of messages that uniSMS has received from this Monitor")
	
	class Meta:
		verbose_name = "Field Monitor"
		ordering = ['last_name']
	
	# the string version of monitors
	# now contains only their name
	def __unicode__(self):
		return "%s %s" %\
			(self.first_name,
			self.last_name)
	
	def _get_latest_report(self):
		try:
			return Entry.objects.filter(monitor=self).order_by('-time')[0]
		
		except IndexError:
			return "N/A"

	latest_report = property(_get_latest_report)
	
	
	
	# 'summarize' the field monitor by
	# returning his full name and number
	def _get_details(self):
		ph = self.phone or "unknown"
		return "%s (%s)" % (self, ph)
	details = property(_get_details)

class Transaction(models.Model):
	identity = models.PositiveIntegerField(blank=True, null=True)
	
	# the monitor (or number, if monitor isn't known)
	# who triggered the creation of this transaction
	# ----
	# todo: validate that at least one of these is provided
	phone = models.CharField(max_length=30, blank=True, null=True)
	monitor = models.ForeignKey(Monitor, blank=True, null=True)
	
	def __unicode__(self):
		
		# prefer to show a monitor,
		# fall back to phone number
		who = self.monitor
		if who is None:
			who = self.phone
		
		# include the random identity and creator
		return "%s by %s" % (unicode(self.identity), who)


class Message(models.Model):
	transaction = models.ForeignKey(Transaction, blank=True, null=True)
	
	# these are only needed if the message is not bound to a
	# transaction, or the message going out to someone else
	phone = models.CharField(max_length=30, blank=True, null=True)
	monitor_id = models.ForeignKey(Monitor, blank=True, null=True)
	
	# but every message has these
	time = models.DateTimeField(auto_now_add=True)
	message = models.CharField(max_length=160)
	is_outgoing = models.BooleanField()
	is_virtual = models.BooleanField()
	
	# todo: what is this for? the screen log?
	def __unicode__(self):
		if self.is_outgoing: dir = ">>"
		else:                dir = "<<"
		return "%s %s: %s" % (dir, self.monitor, self.message)
