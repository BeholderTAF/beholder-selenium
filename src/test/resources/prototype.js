// Função para verificar a confirmação ou cancelamento em um 
		// alerta de confirmação, ou ok em um alerta de aviso
		function alerta_confirmacao() {
			var decisao;
			decisao = confirm("Confirmação de um Alerta");

			if(decisao) {
				alert("Confirmado");
			}
			else {
				alert("Cancelado");
			}
		}