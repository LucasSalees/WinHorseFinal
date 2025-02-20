package com.projeto.sistema.controle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.projeto.sistema.modelos.Usuario;
import com.projeto.sistema.repositorios.UsuarioRepositorio;
import com.projeto.sistema.servico.CookieServico;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Controller
public class PrincipalControle {

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    // Rota para a página inicial
    @RequestMapping("/")
    public String redirectToLogin(HttpSession session) {
        // Verifica se há usuário logado
        if (session.getAttribute("usuarioLogado") != null) {
            return "redirect:/home";
        }
        // Redireciona para a página de login
        return "redirect:/login";
    }

    // Rota para a página de login
    @GetMapping("/login")
    public String acessarLogin(HttpSession session) {
        // Se o usuário já estiver logado, redireciona para a página inicial
        if (session.getAttribute("usuarioLogado") != null) {
            return "redirect:/home";
        }
        // Exibe a página de login
        return "administrativo/login";
    }

    // Login do usuário
    @PostMapping("/login")
    public String login(Model model, Usuario usrParam, /*String lembrar,*/ HttpSession session, HttpServletResponse response) {
        Usuario usr = usuarioRepositorio.Login(usrParam.getEmail(), usrParam.getSenha());

        if (usr != null) {
            // Salva o usuário na sessão
            session.setAttribute("usuarioLogado", usr);
            int tempoLogado = 60*60*24*365;// 1 ano de cookie
            //if(lembrar != null) tempoLogado = (60+60+24+365) // 1 ano de cookie
            // Configura o cookie
            CookieServico.setCookie(response, "id_usuario", String.valueOf(usr.getId_usuario()), tempoLogado);
            return "redirect:/home";
        }

        // Exibe mensagem de erro se o login falhar
        model.addAttribute("erro", "Email ou senha inválidos");
        return "administrativo/login";
    }

    // Página inicial após login
    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        model.addAttribute("usuario", usuario);
        return "administrativo/home";
    }

    // Rota para logout
    @GetMapping("/sair")
    public String logout(HttpSession session, HttpServletResponse response) {
        // Invalida a sessão para limpar os dados
        session.invalidate();
        // Remove o cookie
        CookieServico.setCookie(response, "id_usuario", "", 0);
        // Redireciona para a tela de login
        return "redirect:/login";
    }
    
    
      
    /////////////////////////// 
    // 		  AJUDA			 //
    ///////////////////////////
    
    @GetMapping("/ajuda")
    public String ajuda(HttpSession session, Model model) {
        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            // Redireciona para o login se o usuário não estiver logado
            return "redirect:/login";
        }

        // Recupera o usuário logado da sessão
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        // Adiciona o usuário ao modelo para ser acessado no template
        model.addAttribute("usuario", usuario);

        // Retorna o nome do template para renderizar
        return "administrativo/ajuda";
    }
    
    @GetMapping("/ajudaAdmin")
    public String ajudaAdmin(HttpSession session, Model model) {
        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            // Redireciona para o login se o usuário não estiver logado
            return "redirect:/login";
        }

        // Recupera o usuário logado da sessão
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        // Adiciona o usuário ao modelo para ser acessado no template
        model.addAttribute("usuario", usuario);

        // Retorna o nome do template para renderizar
        return "administrativo/ajudaAdmin";
    }
    
    @GetMapping("/ajudaPerfilUser")
    public String ajudaPerfilUser(HttpSession session, Model model) {
        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            // Redireciona para o login se o usuário não estiver logado
            return "redirect:/login";
        }

        // Recupera o usuário logado da sessão
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        // Adiciona o usuário ao modelo para ser acessado no template
        model.addAttribute("usuario", usuario);

        // Retorna o nome do template para renderizar
        return "administrativo/ajudaPerfilUser";
    }
    
    @GetMapping("/ajudaPerfilAdmin")
    public String ajudaPerfilAdmin(HttpSession session, Model model) {
        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            // Redireciona para o login se o usuário não estiver logado
            return "redirect:/login";
        }

        // Recupera o usuário logado da sessão
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        // Adiciona o usuário ao modelo para ser acessado no template
        model.addAttribute("usuario", usuario);

        // Retorna o nome do template para renderizar
        return "administrativo/ajudaPerfilAdmin";
    }
    
    @GetMapping("/ajudaUser")
    public String ajudaUser(HttpSession session, Model model) {
        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            // Redireciona para o login se o usuário não estiver logado
            return "redirect:/login";
        }

        // Recupera o usuário logado da sessão
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        // Adiciona o usuário ao modelo para ser acessado no template
        model.addAttribute("usuario", usuario);

        // Retorna o nome do template para renderizar
        return "administrativo/ajudaUser";
    }
    
    @GetMapping("/ajudaListaUsuarioAdmin")
    public String ajudaListaUsuarioAdmin(HttpSession session, Model model) {
        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            // Redireciona para o login se o usuário não estiver logado
            return "redirect:/login";
        }

        // Recupera o usuário logado da sessão
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        // Adiciona o usuário ao modelo para ser acessado no template
        model.addAttribute("usuario", usuario);

        // Retorna o nome do template para renderizar
        return "administrativo/ajudaListaUsuarioAdmin";
    }    
    
    @GetMapping("/ajudaEditarUsuarioAdmin")
    public String ajudaEditarUsuarioAdmin(HttpSession session, Model model) {
        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            // Redireciona para o login se o usuário não estiver logado
            return "redirect:/login";
        }

        // Recupera o usuário logado da sessão
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        // Adiciona o usuário ao modelo para ser acessado no template
        model.addAttribute("usuario", usuario);

        // Retorna o nome do template para renderizar
        return "administrativo/ajudaEditarUsuarioAdmin";
    }
    
    @GetMapping("/ajudaHomeAdmin")
    public String ajudaHomeAdmin(HttpSession session, Model model) {
        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            // Redireciona para o login se o usuário não estiver logado
            return "redirect:/login";
        }

        // Recupera o usuário logado da sessão
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        // Adiciona o usuário ao modelo para ser acessado no template
        model.addAttribute("usuario", usuario);

        // Retorna o nome do template para renderizar
        return "administrativo/ajudaHomeAdmin";
    }
    
    @GetMapping("/ajudaCadastroUsuarioAdmin")
    public String ajudaCadastroUsuarioAdmin(HttpSession session, Model model) {
        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            // Redireciona para o login se o usuário não estiver logado
            return "redirect:/login";
        }

        // Recupera o usuário logado da sessão
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        // Adiciona o usuário ao modelo para ser acessado no template
        model.addAttribute("usuario", usuario);

        // Retorna o nome do template para renderizar
        return "administrativo/ajudaCadastroUsuarioAdmin";
    }
    
    @GetMapping("/ajudaCadastroGaranhao")
    public String ajudaCadastroGaranhao(HttpSession session, Model model) {
        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            // Redireciona para o login se o usuário não estiver logado
            return "redirect:/login";
        }

        // Recupera o usuário logado da sessão
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        // Adiciona o usuário ao modelo para ser acessado no template
        model.addAttribute("usuario", usuario);

        // Retorna o nome do template para renderizar
        return "administrativo/ajudaCadastroGaranhao";
    }
    
    @GetMapping("/ajudaListaGaranhao")
    public String ajudaListaGaranhao(HttpSession session, Model model) {
        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            // Redireciona para o login se o usuário não estiver logado
            return "redirect:/login";
        }

        // Recupera o usuário logado da sessão
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        // Adiciona o usuário ao modelo para ser acessado no template
        model.addAttribute("usuario", usuario);

        // Retorna o nome do template para renderizar
        return "administrativo/ajudaListaGaranhao";
    }
    
    @GetMapping("/ajudaEditarGaranhaoAdmin")
    public String ajudaEditarGaranhaoAdmin(HttpSession session, Model model) {
        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            // Redireciona para o login se o usuário não estiver logado
            return "redirect:/login";
        }

        // Recupera o usuário logado da sessão
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        // Adiciona o usuário ao modelo para ser acessado no template
        model.addAttribute("usuario", usuario);

        // Retorna o nome do template para renderizar
        return "administrativo/ajudaEditarGaranhaoAdmin";
    }
    
    @GetMapping("/ajudaListaMovimentacao")
    public String ajudaListaMovimentacao(HttpSession session, Model model) {
        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            // Redireciona para o login se o usuário não estiver logado
            return "redirect:/login";
        }

        // Recupera o usuário logado da sessão
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        // Adiciona o usuário ao modelo para ser acessado no template
        model.addAttribute("usuario", usuario);

        // Retorna o nome do template para renderizar
        return "administrativo/ajudaListaMovimentacao";
    }
    
    @GetMapping("/ajudaCadastroMovimentacao")
    public String ajudaCadastroMovimentacao(HttpSession session, Model model) {
        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            // Redireciona para o login se o usuário não estiver logado
            return "redirect:/login";
        }

        // Recupera o usuário logado da sessão
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        // Adiciona o usuário ao modelo para ser acessado no template
        model.addAttribute("usuario", usuario);

        // Retorna o nome do template para renderizar
        return "administrativo/ajudaCadastroMovimentacao";
    }
    
    @GetMapping("/ajudaEditarMovimentacaoAdmin")
    public String ajudaEditarMovimentacaoAdmin(HttpSession session, Model model) {
        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            // Redireciona para o login se o usuário não estiver logado
            return "redirect:/login";
        }

        // Recupera o usuário logado da sessão
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        // Adiciona o usuário ao modelo para ser acessado no template
        model.addAttribute("usuario", usuario);

        // Retorna o nome do template para renderizar
        return "administrativo/ajudaEditarMovimentacaoAdmin";
    }
    
    /*
     USER 
    */
    
    @GetMapping("/ajudaHomeUser")
    public String ajudaHomeUser(HttpSession session, Model model) {
        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            // Redireciona para o login se o usuário não estiver logado
            return "redirect:/login";
        }

        // Recupera o usuário logado da sessão
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        // Adiciona o usuário ao modelo para ser acessado no template
        model.addAttribute("usuario", usuario);

        // Retorna o nome do template para renderizar
        return "administrativo/ajudaHomeUser";
    }
    
    @GetMapping("/ajudaListaGaranhaoUser")
    public String ajudaListaGaranhaoUser(HttpSession session, Model model) {
        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            // Redireciona para o login se o usuário não estiver logado
            return "redirect:/login";
        }

        // Recupera o usuário logado da sessão
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        // Adiciona o usuário ao modelo para ser acessado no template
        model.addAttribute("usuario", usuario);

        // Retorna o nome do template para renderizar
        return "administrativo/ajudaListaGaranhaoUser";
    }
    
    @GetMapping("/ajudaEditarGaranhaoUser")
    public String ajudaEditarGaranhaoUser(HttpSession session, Model model) {
        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            // Redireciona para o login se o usuário não estiver logado
            return "redirect:/login";
        }

        // Recupera o usuário logado da sessão
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        // Adiciona o usuário ao modelo para ser acessado no template
        model.addAttribute("usuario", usuario);

        // Retorna o nome do template para renderizar
        return "administrativo/ajudaEditarGaranhaoUser";
    }
    
    @GetMapping("/ajudaListaMovimentacaoUser")
    public String ajudaListaMovimentacaoUser(HttpSession session, Model model) {
        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            // Redireciona para o login se o usuário não estiver logado
            return "redirect:/login";
        }

        // Recupera o usuário logado da sessão
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        // Adiciona o usuário ao modelo para ser acessado no template
        model.addAttribute("usuario", usuario);

        // Retorna o nome do template para renderizar
        return "administrativo/ajudaListaMovimentacaoUser";
    }
    
    @GetMapping("/ajudaCadastroMovimentacaoUser")
    public String ajudaCadastroMovimentacaoUser(HttpSession session, Model model) {
        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            // Redireciona para o login se o usuário não estiver logado
            return "redirect:/login";
        }

        // Recupera o usuário logado da sessão
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        // Adiciona o usuário ao modelo para ser acessado no template
        model.addAttribute("usuario", usuario);

        // Retorna o nome do template para renderizar
        return "administrativo/ajudaCadastroMovimentacaoUser";
    }
    
    @GetMapping("/ajudaEditarMovimentacaoUser")
    public String ajudaEditarMovimentacaoUser(HttpSession session, Model model) {
        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            // Redireciona para o login se o usuário não estiver logado
            return "redirect:/login";
        }

        // Recupera o usuário logado da sessão
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        // Adiciona o usuário ao modelo para ser acessado no template
        model.addAttribute("usuario", usuario);

        // Retorna o nome do template para renderizar
        return "administrativo/ajudaEditarMovimentacaoUser";
    }
    
    
}