# 检查并配置Maven环境变量
Write-Host '检查MAVEN_HOME环境变量...'
$mavenHome = [Environment]::GetEnvironmentVariable('MAVEN_HOME', 'User')
if (-not $mavenHome) {
    $mavenHome = [Environment]::GetEnvironmentVariable('MAVEN_HOME', 'Machine')
}

if ($mavenHome) {
    Write-Host '找到MAVEN_HOME: ' $mavenHome
    Write-Host '临时添加Maven到PATH...'
    $env:PATH = $mavenHome + '\bin;' + $env:PATH
    
    Write-Host '检查Maven版本...'
    mvn -v
    
    Write-Host '开始编译...'
    mvn clean package
} else {
    Write-Host '未找到MAVEN_HOME环境变量，请检查配置'
}