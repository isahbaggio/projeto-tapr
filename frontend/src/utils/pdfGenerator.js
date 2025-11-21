import { formatCurrency, formatDate } from './formatters'

export function gerarReciboPDF(venda, cliente) {
  const conteudoHTML = `
    <!DOCTYPE html>
    <html>
    <head>
      <meta charset="UTF-8">
      <title>Recibo de Venda #${venda.id.substring(0, 8)}</title>
      <style>
        body {
          font-family: Arial, sans-serif;
          padding: 20px;
          max-width: 800px;
          margin: 0 auto;
        }
        .header {
          text-align: center;
          border-bottom: 2px solid #333;
          padding-bottom: 20px;
          margin-bottom: 20px;
        }
        .company-name {
          font-size: 24px;
          font-weight: bold;
          margin-bottom: 10px;
        }
        .recibo-title {
          font-size: 18px;
          color: #666;
        }
        .info-section {
          margin: 20px 0;
        }
        .info-row {
          display: flex;
          justify-content: space-between;
          padding: 5px 0;
        }
        .info-label {
          font-weight: bold;
        }
        .items-table {
          width: 100%;
          border-collapse: collapse;
          margin: 20px 0;
        }
        .items-table th {
          background-color: #f0f0f0;
          padding: 10px;
          text-align: left;
          border: 1px solid #ddd;
        }
        .items-table td {
          padding: 8px;
          border: 1px solid #ddd;
        }
        .total-section {
          text-align: right;
          margin-top: 20px;
          font-size: 18px;
        }
        .total-value {
          font-weight: bold;
          font-size: 24px;
          color: #2563eb;
        }
        .footer {
          margin-top: 40px;
          text-align: center;
          color: #666;
          font-size: 12px;
          border-top: 1px solid #ddd;
          padding-top: 20px;
        }
        .status-cancelada {
          color: #dc2626;
          font-weight: bold;
          text-align: center;
          font-size: 20px;
          margin: 20px 0;
        }
        @media print {
          body {
            padding: 0;
          }
        }
      </style>
    </head>
    <body>
      <div class="header">
        <div class="company-name">Oficina Mecânica</div>
        <div class="recibo-title">Recibo de Venda</div>
      </div>

      ${venda.cancelada ? '<div class="status-cancelada">⚠️ VENDA CANCELADA</div>' : ''}

      <div class="info-section">
        <div class="info-row">
          <span class="info-label">Número da Venda:</span>
          <span>#${venda.id.substring(0, 8).toUpperCase()}</span>
        </div>
        <div class="info-row">
          <span class="info-label">Data:</span>
          <span>${formatDate(venda.dataVenda)}</span>
        </div>
        <div class="info-row">
          <span class="info-label">Cliente:</span>
          <span>${cliente?.nome || 'Cliente não informado'}</span>
        </div>
        ${cliente?.cpf ? `
        <div class="info-row">
          <span class="info-label">CPF:</span>
          <span>${cliente.cpf}</span>
        </div>
        ` : ''}
      </div>

      <table class="items-table">
        <thead>
          <tr>
            <th>Tipo</th>
            <th>Item</th>
            <th>Quantidade</th>
            <th>Preço Unit.</th>
            <th>Subtotal</th>
          </tr>
        </thead>
        <tbody>
          ${venda.itens.map(item => `
            <tr>
              <td>${item.tipoItem === 'PRODUTO' ? 'Produto' : 'Serviço'}</td>
              <td>${item.itemNome}</td>
              <td>${item.quantidade}</td>
              <td>R$ ${formatCurrency(item.precoUnitario)}</td>
              <td>R$ ${formatCurrency(item.subtotal)}</td>
            </tr>
          `).join('')}
        </tbody>
      </table>

      ${venda.observacoes ? `
      <div class="info-section">
        <div class="info-label">Observações:</div>
        <div>${venda.observacoes}</div>
      </div>
      ` : ''}

      <div class="total-section">
        <div>Valor Total:</div>
        <div class="total-value">R$ ${formatCurrency(venda.valorTotal)}</div>
      </div>

      <div class="footer">
        <p>Obrigado pela preferência!</p>
        <p>Este documento foi gerado eletronicamente e serve como comprovante de venda.</p>
      </div>
    </body>
    </html>
  `

  const janelaImpressao = window.open('', '_blank')
  janelaImpressao.document.write(conteudoHTML)
  janelaImpressao.document.close()

  janelaImpressao.onload = () => {
    janelaImpressao.print()
  }
}

export function exportarVendasCSV(vendas) {
  const headers = ['Data', 'Cliente ID', 'Valor Total', 'Status', 'Observações']
  const rows = vendas.map(venda => [
    formatDate(venda.dataVenda),
    venda.clienteId,
    formatCurrency(venda.valorTotal),
    venda.cancelada ? 'Cancelada' : 'Ativa',
    venda.observacoes || ''
  ])

  let csvContent = headers.join(',') + '\n'
  rows.forEach(row => {
    csvContent += row.map(cell => `"${cell}"`).join(',') + '\n'
  })

  const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
  const link = document.createElement('a')
  link.href = URL.createObjectURL(blob)
  link.download = `vendas_${new Date().toISOString().split('T')[0]}.csv`
  link.click()
}
