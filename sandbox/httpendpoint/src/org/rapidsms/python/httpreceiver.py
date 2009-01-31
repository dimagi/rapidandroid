import os
import glob
localDir = os.path.dirname(__file__)
absDir = os.path.join(os.getcwd(), localDir)
uploadDir = os.path.join(absDir,'uploads')

import cherrypy
from cherrypy.lib.static import serve_file
from cherrypy.lib import static

class RapidSmsFile(object):    
    def index(self):
        return """
        <html><body>
            <form action="upload" method="post" enctype="multipart/form-data">
            identifier: <input type="text" name="myIdentifier" /><br />
            filename: <input type="file" name="myFile" /><br />
            <input type="submit" />
            </form>
        </body></html>
        """
    index.exposed = True
    @cherrypy.expose
    def upload(self, myIdentifier, myFile):
        out = """<html>
        <body>
            Thank you, %s<br>
            File upload successful<br>
            myFile length: %s<br />
            myFile filename: %s<br />
            myFile mime-type: %s
        </body>
        </html>"""
        
        # Although this just counts the file length, it demonstrates
        # how to read large files in chunks instead of all at once.
        # CherryPy uses Python's cgi module to read the uploaded file
        # into a temporary file; myFile.file.read reads from that.
        size = 0
        os.path.exists(uploadDir)
        print myFile.filename
        fout = open(os.path.join(uploadDir,myFile.filename),'wb')
        while True:
            data = myFile.file.read(8192)
            fout.write(data)
            if not data:
                break
            size += len(data)        
        fout.close()        
        return out % (myIdentifier, size, myFile.filename, myFile.type)
    upload.exposed = True
    
class fileBrowser:   
    @cherrypy.expose
    def index(self, ):
        html = """<html><body><h2>Here are the files in the selected directory:</h2>
        <a href="index?directory=%s">Up</a><br />
        """ % os.path.dirname(os.path.abspath(uploadDir))

        for filename in glob.glob(uploadDir + '/*'):
            absPath = os.path.abspath(filename)
            if os.path.isdir(absPath):
                html += '<a href="/index?directory=' + absPath + '">' + os.path.basename(filename) + "</a> <br />"
            else:
                html += '<a href="/download/?filepath=' + absPath + '">' + os.path.basename(filename) + "</a> <br />"
                
        html += """</body></html>"""
        return html    

class Download:    
    @cherrypy.expose
    def index(self, filepath):
        return serve_file(filepath, "application/x-download", "attachment")        

if __name__ == '__main__':    
    #load up the configuration file
    #alter the port config to reflect that of the settings
    globalconf = {'server.socket_host':'0.0.0.0',
            'server.socket_port': 8160,
            'log.error_file':'mediaserver.error.log'}              
    
    postsconf = {'/posts':{'tools.staticdir.on':True,
                      'tools.staticdir.dir':uploadDir}
                      #'tools.staticdir.index':'index.html'}
                }
    
       
    cherrypy.config.update(globalconf)    
    cherrypy.tree.mount(RapidSmsFile(),'/upload')
    cherrypy.tree.mount(Download(),'/download')
    cherrypy.tree.mount(fileBrowser(), '/posts', postsconf)
    if os.path.exists(uploadDir) == False:
        os.mkdir(uploadDir)

    #try:
    cherrypy.engine.start()
    cherrypy.engine.block() #this allows for control-c shutdown
    #except KeyboardIOnterrupt:
    #    cherrypy.engine.stop()