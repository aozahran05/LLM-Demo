# Start all services for the RAG Full-Stack project locally

Write-Host "=========================================" -ForegroundColor Cyan
Write-Host "  Starting RAG Full-Stack Local Deployment" -ForegroundColor Cyan
Write-Host "=========================================" -ForegroundColor Cyan

# 1. Start Python AI Service (Port 8000)
Write-Host "`n[1/3] Starting Python AI Service (Port 8000)..." -ForegroundColor Yellow
$aiProcess = Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$PSScriptRoot\ai-invoice-agent-main'; python -m uvicorn app:app --port 8000 --reload" -PassThru

# 2. Start Spring Boot Backend (Port 8080)
Write-Host "[2/3] Starting Spring Boot Backend (Port 8080)..." -ForegroundColor Yellow
$backendProcess = Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$PSScriptRoot\RAG-full-stack-main\backend'; .\mvnw.cmd spring-boot:run" -PassThru

# 3. Start Angular Frontend (Port 4200)
Write-Host "[3/3] Starting Angular Frontend (Port 4200)..." -ForegroundColor Yellow
$frontendProcess = Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$PSScriptRoot\RAG-full-stack-main\frontend'; npm start" -PassThru

Write-Host "`nAll 3 services are launching in their own terminal windows!" -ForegroundColor Green
Write-Host "  - Frontend:   http://localhost:4200" -ForegroundColor White
Write-Host "  - Backend:    http://localhost:8080" -ForegroundColor White
Write-Host "  - AI Service: http://localhost:8000" -ForegroundColor White
Write-Host "Note: Remember to set your GEMINI_API_KEY in ai-invoice-agent-main/.env if not already configured." -ForegroundColor Magenta
