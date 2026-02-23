try {
    $response = Invoke-RestMethod -Method Post -Uri "http://localhost:7500/app/member/register" -ContentType "application/json" -InFile "d:\work_boss\dingyangMall\JooLun-wx\register.json"
    $response | ConvertTo-Json -Depth 5
} catch {
    Write-Host "Error calling /app/member/register"
    Write-Host $_.Exception.Response.StatusCode.value__
    Write-Host $_.Exception.Message
    $stream = $_.Exception.Response.GetResponseStream()
    $reader = New-Object System.IO.StreamReader($stream)
    $body = $reader.ReadToEnd()
    Write-Host $body
}