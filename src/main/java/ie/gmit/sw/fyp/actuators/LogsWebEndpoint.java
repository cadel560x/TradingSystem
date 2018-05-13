package ie.gmit.sw.fyp.actuators;

import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.boot.actuate.endpoint.annotation.WriteOperation;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;




@Component
@Endpoint(id="logs")
public class LogsWebEndpoint {
	
	@ReadOperation
	public Resource logFileGET(String logFile) {
		return this.logFile(logFile);
		
	} // end logFileGET(String logFile)
	
	
	@WriteOperation
	public Resource logFile(String logFile) {	
		Resource externalFile = null;
		
		if ( logFile.equals("orders") ) {
			externalFile = new FileSystemResource("orders.log");
		}
		else if ( logFile.equals("matches") ) {
			externalFile = new FileSystemResource("matches.log");
		}
		else if ( logFile.equals("requests") ) {
			externalFile = new FileSystemResource("requests.log");
		}
		
		if (externalFile == null || !externalFile.isReadable()) {
			return null;
		}
		
		return externalFile;
		
	} // end logFile (String logFile)
	
} // end class LogFileWebEndpointExtension
