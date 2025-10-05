@echo off
echo Criando pedido...
curl.exe -X POST "http://localhost:8080/workflow/pedidos?clienteId=maria&valorTotal=50.00" -H "x-user: teste"

echo.
echo.
echo === CAIXA ===
curl.exe http://localhost:8080/caixa

echo.
echo.
echo === ESTOQUE ===
curl.exe http://localhost:8080/estoque

echo.
echo.
echo === PRODUCAO ===
curl.exe http://localhost:8080/producao

echo.
echo.
echo === ENTREGAS ===
curl.exe http://localhost:8080/entregas

echo.
echo.
echo === NOTIFICACOES CLIENTE ===
curl.exe http://localhost:8080/cliente/por-cliente/maria

echo.
echo.
echo === RELATORIO ===
curl.exe http://localhost:8080/relatorio

pause