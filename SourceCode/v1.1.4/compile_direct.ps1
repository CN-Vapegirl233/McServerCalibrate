# 直接设置Maven路径并编译

# 假设Maven安装路径（根据之前的fix_environment.bat文件）
$mavenHome = "D:\Programs\Apache\Maven"

# 检查Maven路径是否存在
if (Test-Path "$mavenHome\bin\mvn.cmd") {
    Write-Host "找到Maven: $mavenHome"
    
    # 临时添加Maven到PATH
    $env:PATH = "$mavenHome\bin;$env:PATH"
    
    # 检查Maven版本
    Write-Host "检查Maven版本..."
    mvn -v
    
    # 执行编译
    Write-Host "开始编译..."
    mvn clean package
    
    # 检查编译结果
    if ($LASTEXITCODE -eq 0) {
        Write-Host "
编译成功！插件文件位于target\sc.jar"
    } else {
        Write-Host "
编译失败，请检查错误信息"
    }
} else {
    Write-Host "未找到Maven安装路径: $mavenHome"
    Write-Host "请检查Maven是否正确安装"
}