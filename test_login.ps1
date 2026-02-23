$response = Invoke-RestMethod -Method Post -Uri "http://localhost:7500/app/member/login" -ContentType "application/json" -InFile "d:\work_boss\dingyangMall\JooLun-wx\login_correct.json"
$token = $response.token
Write-Host "Token: $token"
try {
    $info = Invoke-RestMethod -Method Get -Uri "http://localhost:7500/app/member/info" -Headers @{Authorization = "Bearer $token"}
    $info | ConvertTo-Json -Depth 5
} catch {
    Write-Host "Error calling /app/member/info"
    Write-Host $_.Exception.Response.StatusCode.value__
    Write-Host $_.Exception.Message
}