# Compile the Course Enrolment and Grade Management System
javac -d out src/*.java

if ($LASTEXITCODE -ne 0) {
    Write-Host "Compilation failed." -ForegroundColor Red
    exit $LASTEXITCODE
}

Write-Host "Compilation successful. Starting application..." -ForegroundColor Green
java -cp out Main
