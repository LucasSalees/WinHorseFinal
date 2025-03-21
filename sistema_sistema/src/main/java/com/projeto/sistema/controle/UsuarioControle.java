package com.projeto.sistema.controle;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projeto.sistema.modelos.Usuario;
import com.projeto.sistema.repositorios.UsuarioRepositorio;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
public class UsuarioControle {
	
	@Autowired
    private UsuarioRepositorio usuarioRepositorio;

	// Listar todos os usuários
	@GetMapping("/administrativo/usuarios/listar")
	public ModelAndView listarUsuario(HttpSession session) {
	    // Verifica se o usuário está logado
	    if (session.getAttribute("usuarioLogado") == null) {
	        return new ModelAndView("redirect:/login"); // Redireciona para o login se o usuário não estiver logado
	    }

	    // Obtém o usuário logado da sessão
	    Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

	    // Cria o ModelAndView para a página de listagem
	    ModelAndView mv = new ModelAndView("administrativo/usuarios/lista");

	    // Adiciona o usuário logado ao modelo
	    mv.addObject("usuario", usuario);

	    // Adiciona a lista de usuários ao modelo
	    mv.addObject("listaUsuarios", usuarioRepositorio.findAll());

	    return mv; // Retorna a página de listagem
	}

	// Cadastro de usuário
	@GetMapping("/administrativo/usuarios/cadastro")
	public ModelAndView cadastrar(Usuario usuario, HttpSession session) {
	    // Verifica se o usuário está logado
	    if (session.getAttribute("usuarioLogado") == null) {
	        return new ModelAndView("redirect:/login"); // Redireciona para o login se o usuário não estiver logado
	    }

	    // Obtém o usuário logado da sessão
	    Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

	    // Cria o ModelAndView para a página de cadastro
	    ModelAndView mv = new ModelAndView("/administrativo/usuarios/cadastro");

	    // Adiciona o usuário logado ao modelo (caso queira mostrar alguma informação do usuário logado)
	    mv.addObject("usuarioLogado", usuarioLogado);

	    // Preenche o objeto "usuario" com diasPermitidos (por exemplo, todos os dias da semana)
	    if (usuario.getDiasPermitidos() == null) {
	        usuario.setDiasPermitidos(Arrays.asList("Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado", "Domingo"));
	    }

	    // Adiciona o objeto de usuário ao modelo para o formulário de cadastro
	    mv.addObject("Usuario", usuario);

	    return mv; // Retorna a página de cadastro
	}


	@PostMapping("/administrativo/usuarios/salvar")
	public ModelAndView salvar(@ModelAttribute Usuario usuario, BindingResult result) {
	    // Validação do formulário
	    if (result.hasErrors()) {
	        return cadastrar(usuario, null);  // Retorna para o formulário com os erros
	    }

	    // Definir a data de cadastro (utiliza a data e hora atuais)
	    usuario.setData_cadastro(LocalDateTime.now());

	    // 🖨️ Exibir os valores no terminal
	    System.out.println("USUÁRIO FEZ UM CADASTRO DE UM USUÁRIO");
	    System.out.println("Cadastro de Usuário:");
	    System.out.println("Nome: " + usuario.getNome_usuario());
	    System.out.println("Função: " + usuario.getFuncao());
	    System.out.println("E-mail: " + usuario.getEmail());
	    System.out.println("Senha: " + usuario.getSenha());
	    System.out.println("Tipo de Usuário: " + usuario.getTipo());
	    System.out.println("Dias Permitidos: " + usuario.getDiasPermitidos());
	    System.out.println("Horário Permitido: " + usuario.getHoraInicio() + " até " + usuario.getHoraFim());
	    System.out.println("Data de Cadastro: " + usuario.getData_cadastro());

	    try {
	        // Salvar o usuário no banco de dados
	        usuarioRepositorio.save(usuario);
	    } catch (Exception e) {
	        e.printStackTrace();
	        result.rejectValue("email", "error.usuario", "Erro ao salvar o Usuário. Tente novamente.");
	        System.out.println("Erro ao salvar o Usuário. Tente novamente.");
	        return cadastrar(usuario, null);
	    }

	    // Após salvar com sucesso, redireciona para a listagem dos usuários
	    return new ModelAndView("redirect:/administrativo/usuarios/listar");  
	}

    
    @GetMapping("/administrativo/usuarios/eventoUsuario/editarUsuario/{id_usuario}")
    public String editar(@PathVariable("id_usuario") Long id_usuario, Model model, HttpSession session) {
        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login"; // Redireciona para o login se o usuário não estiver logado
        }

        // Obtém o usuário logado da sessão
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        // Busca o usuário pelo id
        Optional<Usuario> usuario = usuarioRepositorio.findById(id_usuario);

        // Se o usuário for encontrado, exibe a página de edição
        if (usuario.isPresent()) {
            Usuario usuarioAtual = usuario.get();
            
            // Passa o objeto usuário e o usuário logado para o modelo
            model.addAttribute("usuario", usuarioAtual);
            model.addAttribute("usuarioLogado", usuarioLogado);

            // Passa a lista de diasPermitidos para o modelo
            model.addAttribute("diasPermitidos", usuarioAtual.getDiasPermitidos());

            return "administrativo/usuarios/eventoUsuario"; // Retorna para a página de edição
        }

        // Caso não encontre o usuário, redireciona para a lista de usuários
        return "redirect:/administrativo/usuarios/listar";
    }
    
    @GetMapping("/removerUsuario/{id_usuario}")
    public String remover(@PathVariable("id_usuario") Long id_usuario, Model model, HttpSession session) {
        
        // Verifica se o usuário está logado na sessão
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login"; // Redireciona para login se o usuário não estiver logado
        }

        // Obtém o usuário logado da sessão
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");
        model.addAttribute("usuario", usuarioLogado);

        try {
            // Busca o usuário pelo ID
            Optional<Usuario> usuarioOptional = usuarioRepositorio.findById(id_usuario);
            if (usuarioOptional.isPresent()) {
                Usuario usuarioEncontrado = usuarioOptional.get(); // Obtém o usuário
                model.addAttribute("remover", usuarioEncontrado); // Envia o objeto para o modelo

                // Exibir os dados do usuário a ser excluído
                System.out.println("USUÁRIO FEZ UMA TENTATIVA DE EXCLUSÃO DE USUÁRIO");
                System.out.println("USUÁRIO A SER EXCLUÍDO:");
                System.out.println("ID: " + usuarioEncontrado.getId_usuario());
                System.out.println("Nome: " + usuarioEncontrado.getNome_usuario());
                System.out.println("Função: " + usuarioEncontrado.getFuncao());
                System.out.println("E-mail: " + usuarioEncontrado.getEmail());
                System.out.println("Tipo de Usuário: " + usuarioEncontrado.getTipo());
                System.out.println("Dias Permitidos: " + usuarioEncontrado.getDiasPermitidos());
                System.out.println("Horário Permitido: " + usuarioEncontrado.getHoraInicio() + " até " + usuarioEncontrado.getHoraFim());

                // Tenta excluir o usuário
                usuarioRepositorio.deleteById(id_usuario);
                model.addAttribute("message", "Usuário removido com sucesso!");
                System.out.println("STATUS DA EXCLUSÃO:");
                System.out.println("Exclusão confirmada.");
                return "administrativo/usuarios/remover";
            } else {
                // Caso o usuário não seja encontrado
                model.addAttribute("message", "Usuário não encontrado!");
                System.out.println("STATUS DA EXCLUSÃO:");
                System.out.println("ERRO, usuário não encontrado.");
                return "administrativo/usuarios/remover";
            }
        } catch (DataIntegrityViolationException e) {
            // Caso ocorra um erro de violação de integridade no banco de dados
            model.addAttribute("message", "Erro ao excluir: Usuário está vinculado a outras entidades.");
            System.out.println("STATUS DA EXCLUSÃO:");
            System.out.println("ERRO, Usuário está vinculado a outras entidades.");
            return "administrativo/usuarios/remover";
        } catch (Exception e) {
            // Caso qualquer outro erro ocorra
            System.out.println("Erro ao excluir o usuário: " + e.getMessage());
            model.addAttribute("message", "Erro ao excluir: " + e.getMessage());
            System.out.println("STATUS DA EXCLUSÃO:");
            System.out.println("ERRO, Erro ao excluir: " + e.getMessage());
            return "administrativo/usuarios/remover";
        }
    }


    @PostMapping("/administrativo/usuarios/editarUsuario")
    public String salvarEdicaoUsuario(@ModelAttribute("usuario") Usuario usuario, RedirectAttributes redirectAttributes) {
        // Buscar o usuário existente pelo ID
        Optional<Usuario> usuarioExistenteOpt = usuarioRepositorio.findById(usuario.getId_usuario());

        if (usuarioExistenteOpt.isPresent()) {
            Usuario usuarioExistente = usuarioExistenteOpt.get();

            // Exibir os dados antigos antes da alteração
            System.out.println("USUÁRIO FEZ UMA EDIÇÃO DE UM USUÁRIO");
            System.out.println("Dados Antigos do Usuário:");
            System.out.println("Nome: " + usuarioExistente.getNome_usuario());
            System.out.println("Função: " + usuarioExistente.getFuncao());
            System.out.println("E-mail: " + usuarioExistente.getEmail());
            System.out.println("Senha: " + usuarioExistente.getSenha());
            System.out.println("Tipo de Usuário: " + usuarioExistente.getTipo());
            System.out.println("Dias Permitidos: " + usuarioExistente.getDiasPermitidos());
            System.out.println("Horário Permitido: " + usuarioExistente.getHoraInicio() + " até " + usuarioExistente.getHoraFim());

            // Atualizar os campos editáveis do usuário
            usuarioExistente.setNome_usuario(usuario.getNome_usuario());
            usuarioExistente.setEmail(usuario.getEmail());
            usuarioExistente.setSenha(usuario.getSenha());
            usuarioExistente.setTipo(usuario.getTipo());
            usuarioExistente.setFuncao(usuario.getFuncao());
            usuarioExistente.setDiasPermitidos(usuario.getDiasPermitidos()); 
            usuarioExistente.setHoraInicio(usuario.getHoraInicio());
            usuarioExistente.setHoraFim(usuario.getHoraFim());

            // Exibir os novos dados após a alteração
            System.out.println("Novos Dados do Usuário:");
            System.out.println("Nome: " + usuarioExistente.getNome_usuario());
            System.out.println("Função: " + usuarioExistente.getFuncao());
            System.out.println("E-mail: " + usuarioExistente.getEmail());
            System.out.println("Senha: " + usuarioExistente.getSenha());
            System.out.println("Tipo de Usuário: " + usuarioExistente.getTipo());
            System.out.println("Dias Permitidos: " + usuarioExistente.getDiasPermitidos());
            System.out.println("Horário Permitido: " + usuarioExistente.getHoraInicio() + " até " + usuarioExistente.getHoraFim());

            // Salvar as atualizações no banco de dados
            usuarioRepositorio.save(usuarioExistente);

            // Mensagem de sucesso
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Usuário atualizado com sucesso.");
            return "redirect:/administrativo/usuarios/listar";  
        } else {
            // Caso o usuário não seja encontrado
            redirectAttributes.addFlashAttribute("mensagemErro", "Usuário não encontrado.");
            return "redirect:/administrativo/usuarios/listar";
        }
    }


    @PostMapping("/administrativo/usuarios/editarUsuarioSenha")
    public String salvarEdicaoUsuarioSenha(
            @ModelAttribute("usuario") Usuario usuario,
            HttpServletRequest request,
            RedirectAttributes redirectAttributes) {
        
        Optional<Usuario> usuarioExistenteOpt = usuarioRepositorio.findById(usuario.getId_usuario());

        if (usuarioExistenteOpt.isPresent()) {
            Usuario usuarioExistente = usuarioExistenteOpt.get();
            usuarioExistente.setSenha(usuario.getSenha());
            usuarioRepositorio.save(usuarioExistente);

            // Mensagem de sucesso
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Senha alterada com sucesso!");
        } else {
            redirectAttributes.addFlashAttribute("mensagemErro", "Usuário não encontrado.");
        }

        // Obtém a URL da página anterior
        String referer = request.getHeader("Referer");
        
        // Se não houver referer, redireciona para uma página padrão
        if (referer == null || referer.isEmpty()) {
            referer = "/administrativo/usuarios";
        }

        return "redirect:" + referer;
    }


    @GetMapping("/administrativo/usuarios/perfil")
    public String perfilUsuario(Model model, HttpSession session) {
        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login"; // Redireciona para o login se o usuário não estiver logado
        }

     // Obtém o usuário logado da sessão
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        System.out.println("Hora Fim do Usuário: " + usuario.getHoraFim()); // Verifica se o valor existe

        if (usuario.getHoraFim() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            model.addAttribute("horaFim", usuario.getHoraFim().format(formatter));
        } else {
            model.addAttribute("horaFim", ""); // Evita erro caso seja null
        }
        
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        // Adiciona as informações do usuário ao modelo
        model.addAttribute("usuario", usuarioLogado);

        // Passa a lista de diasPermitidos e horários para o modelo
        model.addAttribute("diasPermitidos", usuarioLogado.getDiasPermitidos());
        model.addAttribute("horaInicio", usuarioLogado.getHoraInicio());  // Adicionando hora de início
        model.addAttribute("horaFim", usuarioLogado.getHoraFim());        // Adicionando hora de fim

        // Retorna para a página de perfil
        return "administrativo/usuarios/perfil"; // Nome do HTML a ser exibido
    }



}