package ifrn.pi.eventos.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import ifrn.pi.eventos.models.Convidado;
import ifrn.pi.eventos.models.Evento;
import ifrn.pi.eventos.repositories.ConvidadoRepository;
import ifrn.pi.eventos.repositories.EventoRepository;
import jakarta.persistence.Entity;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/eventos")
public class EventosControlle {

	@Autowired
	private EventoRepository er;
	@Autowired
	private ConvidadoRepository cr;
	
	@GetMapping("/formEvento")
	public String form(Evento evento) {
		return "eventos/formEvento";
	}

	@PostMapping
	public String salvar(@Valid Evento evento, BindingResult result, RedirectAttributes attributes) {

		if(result.hasErrors()) {
			return form(evento);
		}
		
		System.out.println(evento);
		er.save(evento);

		attributes.addFlashAttribute("mensagem", "Evento salvo com sucesso!");
		
		return "redirect:/eventos";
	}

	@GetMapping
	public ModelAndView listar() {
		List<Evento> eventos = er.findAll();
		ModelAndView mv = new ModelAndView("eventos/lista");
		mv.addObject("eventos", eventos);
		return mv;
	}

	@GetMapping("/{id}")
	public ModelAndView detalhar(@PathVariable Long id) {
	    
	    ModelAndView md = new ModelAndView();
	    
	    Optional<Evento> opt = er.findById(id);
	    
	    if (opt.isEmpty()) {
	        md.setViewName("redirect:/eventos");
	        return md;
	    }

	    Evento evento = opt.get();
	    
	    md.setViewName("eventos/detalhes");
	    md.addObject("evento", evento);
	    md.addObject("convidado", new Convidado());
	    
	    List<Convidado> convidados = cr.findByEvento(evento);
	    md.addObject("convidados", convidados);
	    
	    return md;
	}
	
	@PostMapping("/{idEvento}")
	
	public ModelAndView salvarConvidado(@PathVariable Long idEvento, @Valid Convidado convidado, BindingResult result, RedirectAttributes attributes) {

	    Optional<Evento> opt = er.findById(idEvento);

	    if (opt.isEmpty()) {
	        return new ModelAndView("redirect:/eventos");
	    }

	    Evento evento = opt.get();

	    if (result.hasErrors()) {
	        ModelAndView md = new ModelAndView("eventos/detalhes");

	        md.addObject("evento", evento);
	        md.addObject("convidado", convidado);
	        md.addObject("convidados", cr.findByEvento(evento));

	        return md;
	    }

	    convidado.setEvento(evento);
	    cr.save(convidado);

	    attributes.addFlashAttribute("mensagem", "Convidado salvo com sucesso!");
	    
	

	    return new ModelAndView("redirect:/eventos/" + idEvento);
	}
	
	@GetMapping("/{id}/selecionar")
	public ModelAndView selecionarEvento(@PathVariable Long id) {
		ModelAndView md = new ModelAndView();
		Optional<Evento> opt = er.findById(id);
		if(opt.isEmpty()) {
			md.setViewName("redirect:/eventos"); 
			return md; 
		}
		
		Evento evento = opt.get();
		md.setViewName("eventos/formEvento");
		md.addObject("evento", evento);
		
		return md;
		
	}
	
	@GetMapping("/{idEvento}/convidados/{idConvidado}/selecionar")
	public ModelAndView selecionarConvidado(@PathVariable Long idEvento, @PathVariable Long idConvidado) {
		
		ModelAndView md = new ModelAndView();
		
		Optional<Evento> optEvento = er.findById(idEvento);
		Optional<Convidado> optConvidado = cr.findById(idConvidado);
		
		if(optEvento.isEmpty() || optConvidado.isEmpty()) {
			md.setViewName("redirect:/eventos");
			return md;
		}
		
		Evento evento = optEvento.get();
		Convidado convidado = optConvidado.get();
		 
		if(evento.getId() != convidado.getEvento().getId()) {
			return md;
		}
		md.setViewName("eventos/detalhes");
		md.addObject("convidado", convidado);
		md.addObject("evento", evento);
		md.addObject("convidados", cr.findByEvento(evento));
		
		return md;
				
		
	}
	
	@GetMapping("/{id}/remover")
	
	public String apagarEvento(@PathVariable Long id, RedirectAttributes attributes) {
		
		Optional<Evento> opt = er.findById(id);
		
		if(!opt.isEmpty()) {
			Evento evento = opt.get();
			
			List<Convidado> convidados = cr.findByEvento(evento);
			
			cr.deleteAll(convidados);
			
			er.delete(evento);
			
			attributes.addFlashAttribute("mensagem", "Evento removido com sucesso!");
		}
		
		return "redirect:/eventos";
		
	}
	@GetMapping("/convidado/{id}/remover")
	public String apagarConvidado(@PathVariable Long id, RedirectAttributes attributes) {

	    Optional<Convidado> opt = cr.findById(id);

	    if (!opt.isEmpty()) {
	        Convidado convidado = opt.get();
	        Long idEvento = convidado.getEvento().getId();

	        cr.delete(convidado);

	        attributes.addFlashAttribute("mensagem", "Convidado removido com sucesso!");

	        return "redirect:/eventos/" + idEvento;
	    }

	    return "redirect:/eventos";
	}
	}

