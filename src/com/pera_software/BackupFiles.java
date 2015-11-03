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
import java.util.*;
import com.pera_software.aidkit.Console;
import com.pera_software.aidkit.lang.*;
import com.pera_software.aidkit.nio.file.*;

// To replace backslash with forward slash '%s:\\:/:gc'

//##############################################################################

public class BackupFiles
{
	private static final String JAZZ_WORKSPACE_DIRECTORY = "C:/JazzWorkspaceSeparationDevelopment/";
	private static final String CLEARCASE_SNAPSHOT_DIRECTORY = "C:/ClearCaseViews/mcttools_view/";

	private static final String CPLUSPLUS_WILDCARD = "*.{h,hpp,cpp}";
	private static final String JAVA_WILDCARD = "*.java";

	private static final String FIREFOX_PROFILE_DIRECTORY = SystemEnvironment.settingsDirectory() + "/Mozilla/Firefox/Profiles/v1w7219y.default";

	private static final String configurationFiles[] = {

		SystemEnvironment.systemDrive() + "/AUTOEXEC.BAT",

		// Vim:

		SystemEnvironment.programsDirectory() + "/Vim/_vimrc",
		SystemEnvironment.programsDirectory() + "/Vim/vimfiles/after/syntax/c.vim",
		SystemEnvironment.programsDirectory() + "/Vim/vimfiles/after/syntax/java.vim",
		SystemEnvironment.programsDirectory() + "/Vim/vimfiles/after/colors/peter_most_minimal.vim",
		SystemEnvironment.settingsDirectory() + "/Microsoft/Windows/SendTo/gVim.lnk",

		// Firefox settings and passwords:

		FIREFOX_PROFILE_DIRECTORY + "/places.sqlite",  // Bookmarks, Downloads and Browsing History:
		FIREFOX_PROFILE_DIRECTORY + "/bookmarks.html", // Exported Bookmarks (about:config 'browser.bookmarks.autoExportHTML')

		FIREFOX_PROFILE_DIRECTORY + "/prefs.js",       // Preferences/Settings
		FIREFOX_PROFILE_DIRECTORY + "/key3.db",        // Passwords
		FIREFOX_PROFILE_DIRECTORY + "/signons.sqlite", // Saved passwords
	};

	private static final String sourceFiles[] =  {
        // C++:

		JAZZ_WORKSPACE_DIRECTORY + "CMWmars/Utilities/UtilityCpp/" + CPLUSPLUS_WILDCARD,
		JAZZ_WORKSPACE_DIRECTORY + "CMWmars/Utilities/UtilityCppUnitTest/" + CPLUSPLUS_WILDCARD,

        // Java:

		CLEARCASE_SNAPSHOT_DIRECTORY + "crtu_meg/modules/utilities/java/com/rohde_schwarz/crtu/lib/calls/" + JAVA_WILDCARD,
		CLEARCASE_SNAPSHOT_DIRECTORY + "crtu_meg/modules/utilities/java/com/rohde_schwarz/crtu/lib/streams/" + JAVA_WILDCARD,
		CLEARCASE_SNAPSHOT_DIRECTORY + "crtu_meg/modules/utilities/java/com/rohde_schwarz/crtu/lib/util/" + JAVA_WILDCARD,
		CLEARCASE_SNAPSHOT_DIRECTORY + "crtu_meg/modules/utilities/java/com/rohde_schwarz/log4j/" + JAVA_WILDCARD,
		CLEARCASE_SNAPSHOT_DIRECTORY + "crtu_meg/modules/utilities/java/com/rohde_schwarz/meg/" + JAVA_WILDCARD,
		CLEARCASE_SNAPSHOT_DIRECTORY + "crtu_meg/modules/utilities/unittests/com/rohde_schwarz/" + JAVA_WILDCARD,

		// Misc:

		"O:/PMost_Privat/Snippets/*.*"
	};

	private static final String mediaFiles[] = {
		"O:/PMost_Privat/Bilder/*.*",
		"O:/PMost_Privat/Musik/*.*"
	};


	//============================================================================

	private static String makeIsoDate()
	{
		Calendar calendar = Calendar.getInstance();

		String isoDate = String.format( "%04d-%02d-%02d", calendar.get( Calendar.YEAR ),
			calendar.get( Calendar.MONTH ) + 1, calendar.get( Calendar.DAY_OF_MONTH ));

		return ( isoDate );
	}

	//============================================================================

	private static void backupFiles( String zipFileNamePrefix, String wildCardPatterns[] )
		throws Exception
	{
		String zipFileName = zipFileNamePrefix + makeIsoDate() + ".zip";
		try ( FileBackup backup = new FileBackup( zipFileName )) {
			backup.storingFileSignal.connect( file -> {
				Console.printStatus( "Storing: %s", file );
			});
			backup.storingFileFailedSignal.connect(( fileName, exception ) -> {
				if ( exception instanceof FileNotFoundException ) {
					Console.printError( "File/Directory not found: %s", fileName );
				} else {
					Console.printError( "File/Directory '%s' not stored because: '%s'", fileName, exception.getMessage() );
				}
			});
			Console.printStatus( "Created: " + zipFileName );
			for ( String wildCardPattern : wildCardPatterns )
				backup.backup( wildCardPattern );
		}
		Console.printStatus( "Closed: " + zipFileName );
	}

	//============================================================================

	public static void main( String arguments[] )
		throws Exception
	{
		// Backup all files to a zip file:

		backupFiles( "../Source_", sourceFiles );
		backupFiles( "../Configuration_", configurationFiles );
		backupFiles( "../Media_", mediaFiles );
	}
}
