# Set JAVA_HOME and Maven path then compile

# Set Java home (based on previous information)
$javaHome = "c:\program files\microsoft\jdk-21.0.5.11-hotspot"

# Set Maven home (from fix_environment.bat)
$mavenHome = "D:\Programs\Apache\Maven"

# Check if Java path exists
if (Test-Path "$javaHome\bin\java.exe") {
    Write-Host "Found Java: $javaHome"
    $env:JAVA_HOME = $javaHome
} else {
    Write-Host "Java not found at: $javaHome"
    Write-Host "Please check Java installation"
    exit 1
}

# Check if Maven path exists
if (Test-Path "$mavenHome\bin\mvn.cmd") {
    Write-Host "Found Maven: $mavenHome"
    $env:PATH = "$mavenHome\bin;$env:PATH"
} else {
    Write-Host "Maven not found at: $mavenHome"
    Write-Host "Please check Maven installation"
    exit 1
}

# Check Java version
Write-Host "Checking Java version..."
java -version

# Check Maven version
Write-Host "Checking Maven version..."
mvn -v

# Execute compilation
Write-Host "Starting compilation..."
mvn clean package

# Check compilation result
if ($LASTEXITCODE -eq 0) {
    Write-Host "
Compilation successful! Plugin file is in target\sc.jar"
} else {
    Write-Host "
Compilation failed, please check error messages"
}