package ifrn.pi.eventos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import ifrn.pi.eventos.models.Evento;
import ifrn.pi.eventos.repositories.EventoRepository;

@Controller	
public class EventosControlle {

	@Autowired
	private EventoRepository er;
	
	@RequestMapping("/eventos/formEvento")
	public String form() {
		return "eventos/formEvento";
	}
	
	@PostMapping("/eventos") 
	public String adicionar (Evento evento) {
		
		System.out.println(evento);
		er.save(evento);
		
		return "eventos/evento-adicionado";
	}
}
