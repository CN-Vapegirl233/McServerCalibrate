# Directly set Maven path and compile

# Assume Maven installation path (from fix_environment.bat)
$mavenHome = "D:\Programs\Apache\Maven"

# Check if Maven path exists
if (Test-Path "$mavenHome\bin\mvn.cmd") {
    Write-Host "Found Maven: $mavenHome"
    
    # Temporarily add Maven to PATH
    $env:PATH = "$mavenHome\bin;$env:PATH"
    
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
} else {
    Write-Host "Maven installation not found at: $mavenHome"
    Write-Host "Please check if Maven is correctly installed"
}