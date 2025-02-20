package com.projeto.sistema.controle;

import java.time.LocalDateTime;
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

	    // Adiciona o objeto de usuário ao modelo para o formulário de cadastro
	    mv.addObject("Usuario", usuario);

	    return mv; // Retorna a página de cadastro
	}

    @PostMapping("/administrativo/usuarios/salvar")
    public ModelAndView salvar(@ModelAttribute Usuario usuario, BindingResult result) {
        // Validação do formulário
        if (result.hasErrors()) {
            // Se houver erros de validação, volta para o formulário com os erros
            return cadastrar(usuario, null);  // Retorna para o formulário com os erros
        }

        // Definir a data de cadastro (utiliza a data e hora atuais)
        usuario.setData_cadastro(LocalDateTime.now());

        // Salvar o usuário no banco de dados
        usuarioRepositorio.save(usuario);

        // Após salvar com sucesso, redireciona para a listagem dos usuários
        return new ModelAndView("redirect:/administrativo/usuarios/listar");  // Redireciona para a listagem após salvar
    }
    
    // Editar um garanhão específico pelo ID
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
            model.addAttribute("usuario", usuario.get()); // Adiciona o objeto usuário ao modelo
            model.addAttribute("usuarioLogado", usuarioLogado); // Adiciona o usuário logado ao modelo
            return "administrativo/usuarios/eventoUsuario"; // Retorna para a página de edição
        }

        // Caso não encontre o usuário, redireciona para a lista de usuários
        return "redirect:/administrativo/usuarios/listar";
    }
    
    @GetMapping("/removerUsuario/{id_usuario}")
    public String remover(@PathVariable("id_usuario") Long id_usuario, Model model, HttpSession session) {
        System.out.println("ID recebido para exclusão: " + id_usuario); // Log para verificar o ID

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
                // Tenta excluir o usuário
                usuarioRepositorio.deleteById(id_usuario);
                model.addAttribute("message", "Usuário removido com sucesso!");
            } else {
                model.addAttribute("message", "Usuário não encontrado!");
            }
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("message", "Erro ao excluir: Usuário está vinculado a outras entidades.");
        } catch (Exception e) {
            System.out.println("Erro ao excluir o usuário: " + e.getMessage());
            model.addAttribute("message", "Erro ao excluir: " + e.getMessage());
        }

        return "administrativo/usuarios/remover"; // Retorna para a mesma página com mensagem
    }

    @PostMapping("/administrativo/usuarios/editarUsuario")
    public String salvarEdicaoUsuario(@ModelAttribute("usuario") Usuario usuario, RedirectAttributes redirectAttributes) {
        // Buscar o usuário existente pelo ID
        Optional<Usuario> usuarioExistenteOpt = usuarioRepositorio.findById(usuario.getId_usuario());
        
        if (usuarioExistenteOpt.isPresent()) {
            Usuario usuarioExistente = usuarioExistenteOpt.get();

            // Atualizar os campos editáveis do usuário
            usuarioExistente.setNome_usuario(usuario.getNome_usuario());
            usuarioExistente.setEmail(usuario.getEmail());
            usuarioExistente.setSenha(usuario.getSenha());
            usuarioExistente.setTipo(usuario.getTipo());
            usuarioExistente.setData_cadastro(usuario.getData_cadastro());
            usuarioExistente.setCelular(usuario.getCelular());
            usuarioExistente.setCpf(usuario.getCpf());
            usuarioExistente.setEndereco(usuario.getEndereco());
            usuarioExistente.setFuncao(usuario.getFuncao());

            // Salvar as atualizações no banco de dados
            usuarioRepositorio.save(usuarioExistente);

            // Mensagem de sucesso
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Usuário atualizado com sucesso.");
            return "redirect:/administrativo/usuarios/listar"; // Redireciona para a lista de usuários
        } else {
            // Caso o usuário não seja encontrado
            redirectAttributes.addFlashAttribute("mensagemErro", "Usuário não encontrado.");
            return "redirect:/administrativo/usuarios/listar";
        }
    }
    
    @GetMapping("/administrativo/usuarios/perfil")
    public String perfilUsuario(Model model, HttpSession session) {
        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login"; // Redireciona para o login se o usuário não estiver logado
        }

        // Obtém o usuário logado da sessão
        Usuario usuarioLogado = (Usuario) session.getAttribute("usuarioLogado");

        // Adiciona as informações do usuário ao modelo
        model.addAttribute("usuario", usuarioLogado);

        // Retorna para a página de perfil
        return "administrativo/usuarios/perfil"; // Nome do HTML a ser exibido
    }

}