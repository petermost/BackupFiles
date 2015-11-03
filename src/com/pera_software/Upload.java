// Copyright 2014 Peter Most, PERA Software Solutions GmbH
//
// This file is part of the BackupFiles program.
//
// BackupFiles is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// BackupFiles is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with BackupFiles. If not, see <http://www.gnu.org/licenses/>.

package com.pera_software;

import java.io.*;

import org.apache.commons.net.ftp.*;

// Try to upload it:

//String passWord = readPassWord( "FTP password" );
//if ( passWord != null ) {
//	try ( Upload upload = new Upload( "example.com", "<userName>", passWord )) {
//		upload.upload( zipFileName );
//	} catch ( Exception exception) {
//		Console.printError( "Uploading failed with: %s",  exception.getMessage() );
//	}
//}

public class Upload implements Closeable
{
	private FTPHTTPClient _ftp;

	public Upload( String serverName, String userName, String passWord )
		throws Exception
	{
		if ( passWord.isEmpty() )
			passWord = "xxx";

		_ftp = new FTPHTTPClient( "<proxyHostName>", 80, "<userName>", "<userPassWord>" );
		_ftp.connect( serverName );
		_ftp.login( userName, passWord );
		_ftp.enterLocalActiveMode();
	}

	@Override
	public void close() throws IOException
	{
		_ftp.logout();
		_ftp.disconnect();
	}

	public boolean upload( String fileName )
		throws Exception
	{
		try ( InputStream fileInputStream = new FileInputStream( fileName )) {
			return _ftp.storeFile( "/backup/" + fileName, fileInputStream );
		}
	}
}
