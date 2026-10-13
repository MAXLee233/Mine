$ErrorActionPreference = 'Stop'
$mineRoot = Split-Path -Parent $PSScriptRoot
$mineCheckDir = Join-Path $mineRoot '.verification'
New-Item -ItemType Directory -Force -Path $mineCheckDir | Out-Null
$mineJunit = Join-Path $mineCheckDir 'junit-4.12.jar'
$mineHamcrest = Join-Path $mineCheckDir 'hamcrest-core-1.3.jar'
if (-not (Test-Path -LiteralPath $mineJunit)) {
    Invoke-WebRequest 'https://repo.maven.apache.org/maven2/junit/junit/4.12/junit-4.12.jar' -OutFile $mineJunit
}
if (-not (Test-Path -LiteralPath $mineHamcrest)) {
    Invoke-WebRequest 'https://repo.maven.apache.org/maven2/org/hamcrest/hamcrest-core/1.3/hamcrest-core-1.3.jar' -OutFile $mineHamcrest
}
$mineClasses = Join-Path $mineCheckDir 'classes'
$mineSources = @(
    (Join-Path $mineRoot 'app/src/main/java/com/coderpage/mine/app/tally/module/edit/record/CommonNotes.java'),
    (Join-Path $mineRoot 'app/src/test/java/com/coderpage/mine/app/tally/module/edit/record/CommonNotesTest.java'),
    (Join-Path $mineRoot 'app/src/test/java/com/coderpage/mine/ExampleUnitTest.java')
)
& javac -encoding UTF-8 -cp "$mineJunit;$mineHamcrest" -d $mineClasses @mineSources
if ($LASTEXITCODE -ne 0) { throw 'Java compilation failed.' }
& java '-Dfile.encoding=UTF-8' -cp "$mineClasses;$mineJunit;$mineHamcrest" org.junit.runner.JUnitCore `
    com.coderpage.mine.app.tally.module.edit.record.CommonNotesTest com.coderpage.mine.ExampleUnitTest
if ($LASTEXITCODE -ne 0) { throw 'Unit tests failed.' }
Write-Output 'Standalone Java tests passed. Android build and device testing are still required.'
