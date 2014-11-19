;This file will be executed next to the application bundle image
;I.e. current directory will contain folder WSeminar with application files
[Setup]
AppId={{fxApplication}}
AppName=WSeminar
AppVersion=1.0
AppVerName=WSeminar 1.0
AppPublisher=Dakror
AppComments=WSeminar
AppCopyright=Copyright (C) 2014
;AppPublisherURL=http://java.com/
;AppSupportURL=http://java.com/
;AppUpdatesURL=http://java.com/
DefaultDirName={pf32}\Dakror\WSeminar
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
OutputBaseFilename=WSeminar
Compression=lzma
SolidCompression=yes
PrivilegesRequired=lowest
SetupIconFile=WSeminar\WSeminar.ico
UninstallDisplayIcon={app}\WSeminar.ico
UninstallDisplayName=WSeminar
WizardImageStretch=No
WizardSmallImageFile=WSeminar-setup-icon.bmp   
ArchitecturesInstallIn64BitMode=x64

[Languages]
Name: "deutsch"; MessagesFile: "compiler:Languages\German.isl"

[Files]
Source: "WSeminar\WSeminar.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "WSeminar\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{group}\WSeminar"; Filename: "{app}\WSeminar.exe"; IconFilename: "{app}\WSeminar.ico"; Check: returnTrue()
Name: "{commondesktop}\WSeminar"; Filename: "{app}\WSeminar.exe";  IconFilename: "{app}\WSeminar.ico"; Check: returnTrue()

[Run]
Filename: "{app}\WSeminar.exe"; Description: "{cm:LaunchProgram,WSeminar}"; Flags: nowait postinstall skipifsilent; Check: returnTrue()
Filename: "{app}\WSeminar.exe"; Parameters: "-install -svcName ""WSeminar"" -svcDesc ""WSeminar"" -mainExe ""WSeminar.exe""  "; Check: returnFalse()

[UninstallRun]
Filename: "{app}\WSeminar.exe "; Parameters: "-uninstall -svcName WSeminar -stopOnUninstall"; Check: returnFalse()

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
