;This file will be executed next to the application bundle image
;I.e. current directory will contain folder PathFinder with application files
[Setup]
AppId={{fxApplication}}
AppName=PathFinder
AppVersion=1.0
AppVerName=PathFinder 1.0
AppPublisher=Dakror
AppComments=PathFinder
AppCopyright=Copyright (C) 2014
;AppPublisherURL=http://java.com/
;AppSupportURL=http://java.com/
;AppUpdatesURL=http://java.com/
DefaultDirName={pf32}\Dakror\PathFinder
DisableStartupPrompt=Yes
DisableDirPage=No
DisableProgramGroupPage=Yes
DisableReadyPage=Yes
DisableFinishedPage=No
DisableWelcomePage=No
DefaultGroupName=Dakror
;Optional License
LicenseFile=
;WinXP or above
MinVersion=0,5.1 
OutputBaseFilename=PathFinder
Compression=lzma
SolidCompression=yes
PrivilegesRequired=admin
SetupIconFile=PathFinder\PathFinder.ico
UninstallDisplayIcon={app}\PathFinder.ico
UninstallDisplayName=PathFinder
WizardImageStretch=No
WizardSmallImageFile=PathFinder-setup-icon.bmp   
ArchitecturesInstallIn64BitMode=x64

[Languages]
Name: "deutsch"; MessagesFile: "compiler:Languages\German.isl"

[Files]
Source: "PathFinder\PathFinder.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "PathFinder\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{group}\PathFinder"; Filename: "{app}\PathFinder.exe"; IconFilename: "{app}\PathFinder.ico"; Check: returnTrue()
Name: "{commondesktop}\PathFinder"; Filename: "{app}\PathFinder.exe";  IconFilename: "{app}\PathFinder.ico"; Check: returnTrue()

[Run]
Filename: "{app}\PathFinder.exe"; Description: "{cm:LaunchProgram,PathFinder}"; Flags: nowait postinstall skipifsilent; Check: returnTrue()
Filename: "{app}\PathFinder.exe"; Parameters: "-install -svcName ""PathFinder"" -svcDesc ""PathFinder"" -mainExe ""PathFinder.exe""  "; Check: returnFalse()

[UninstallRun]
Filename: "{app}\PathFinder.exe "; Parameters: "-uninstall -svcName PathFinder -stopOnUninstall"; Check: returnFalse()

[Code]
function returnTrue(): Boolean;
begin
  Result := True;
end;

function returnFalse(): Boolean;
begin
  Result := False;
end;

function InitializeSetup(): Boolean;
begin
// Possible future improvements:
//   if version less or same => just launch app
//   if upgrade => check if same app is running and wait for it to exit
//   Add pack200/unpack200 support? 
  Result := True;
end;  
