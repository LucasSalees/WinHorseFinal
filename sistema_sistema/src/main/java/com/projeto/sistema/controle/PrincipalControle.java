package com.projeto.sistema.controle;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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

    @PostMapping("/login")
    public String login(Model model, Usuario usrParam, HttpSession session, HttpServletResponse response) {
        Usuario usr = usuarioRepositorio.Login(usrParam.getEmail(), usrParam.getSenha());

        if (usr != null) {
            // Verificar se o usuário tem restrição de horário e dias
            LocalTime agora = LocalTime.now();
            DayOfWeek diaAtual = LocalDate.now().getDayOfWeek();

            // Converter dia atual para o nome do dia em português
            String nomeDia = converterDiaParaPortugues(diaAtual);

            // Imprimir no console o dia atual, os dias permitidos e as horas
            System.out.println("Usuário conectado.");
            System.out.println("ID do usuário: " + usr.getId_usuario()); // Exibe o ID do usuário
            System.out.println("Nome do usuário: " + usr.getNome_usuario()); // Exibe o nome do usuário
            System.out.println("Dia atual: " + nomeDia); // Exibe o dia atual em português
            System.out.println("Dias permitidos: " + usr.getDiasPermitidos()); // Exibe os dias permitidos
            System.out.println("Hora atual: " + agora); // Exibe a hora atual
            System.out.println("Hora início permitida: " + usr.getHoraInicio()); // Exibe a hora de início permitida
            System.out.println("Hora fim permitida: " + usr.getHoraFim()); // Exibe a hora de fim permitida

            // Verificar se o dia está na lista de dias permitidos
            if (usr.getDiasPermitidos() != null && !usr.getDiasPermitidos().contains(nomeDia)) {
                model.addAttribute("erro", "Data com restrição de acesso, contate seu administrador.");
                return "administrativo/login";
            }

            // Verificar se o horário atual está dentro do permitido
            if (usr.getHoraInicio() != null && usr.getHoraFim() != null) {
                if (agora.isBefore(usr.getHoraInicio()) || agora.isAfter(usr.getHoraFim())) {
                    model.addAttribute("erro", "Horário com restrição de acesso, contate seu administrador.");
                    return "administrativo/login";
                }
            }

            // Se tudo estiver correto, salva o usuário na sessão
            session.setAttribute("usuarioLogado", usr);
            int tempoLogado = 60 * 60 * 24 * 365; // 1 ano de cookie
            CookieServico.setCookie(response, "id_usuario", String.valueOf(usr.getId_usuario()), tempoLogado);

            return "redirect:/home";
        }

        // Se email ou senha estiverem errados
        model.addAttribute("erro", "Email ou senha inválidos.");
        return "administrativo/login";
    }

    // Função para converter o dia da semana para o nome em português
    public String converterDiaParaPortugues(DayOfWeek dia) {
        switch (dia) {
            case MONDAY: return "Segunda";
            case TUESDAY: return "Terça";
            case WEDNESDAY: return "Quarta";
            case THURSDAY: return "Quinta";
            case FRIDAY: return "Sexta";
            case SATURDAY: return "Sábado";
            case SUNDAY: return "Domingo";
            default: return "";
        }
    }


    @GetMapping("/home")
    public String home(HttpSession session, Model model) {
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
        }
        
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        System.out.println("Hora Fim do Usuário: " + usuario.getHoraFim()); // Verifica se o valor existe

        if (usuario.getHoraFim() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            model.addAttribute("horaFim", usuario.getHoraFim().format(formatter));
        } else {
            model.addAttribute("horaFim", ""); // Evita erro caso seja null
        }

        model.addAttribute("usuario", usuario);
        return "administrativo/home";
    }


    // Rota para logout
    @GetMapping("/sair")
    public String logout(HttpSession session, HttpServletResponse response) {
        // Hora atual quando o usuário sai
        LocalTime agora = LocalTime.now();
        
        // Imprimir no console que o usuário saiu e a hora da saída
        System.out.println("Usuário saiu às: " + agora); // Exibe a hora da saída
        System.out.println("Usuário desconectado.");

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