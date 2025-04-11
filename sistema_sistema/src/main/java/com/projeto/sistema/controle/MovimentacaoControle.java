
package com.projeto.sistema.controle;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import com.projeto.sistema.modelos.Garanhao;
import com.projeto.sistema.modelos.Lixeira;
import com.projeto.sistema.modelos.Movimentacao;
import com.projeto.sistema.modelos.Usuario;
import com.projeto.sistema.repositorios.GaranhaoRepositorio;
import com.projeto.sistema.repositorios.LixeiraRepositorio;
import com.projeto.sistema.repositorios.MovimentacaoRepositorio;
import com.projeto.sistema.repositorios.UsuarioRepositorio;

import jakarta.servlet.http.HttpSession;

@Controller
public class MovimentacaoControle {

    @Autowired
    private MovimentacaoRepositorio movimentacaoRepositorio;

    @Autowired
    private GaranhaoRepositorio garanhaoRepositorio;
    
    @Autowired
    private LixeiraRepositorio lixeiraRepositorio;
    
    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    // Página de cadastro de movimentação
    @GetMapping("/administrativo/movimentacoes/cadastro")
    public ModelAndView cadastrar(Movimentacao movimentacao, HttpSession session, Model model) {
        ModelAndView mv = new ModelAndView("/administrativo/movimentacoes/cadastro");

        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            mv.setViewName("redirect:/login"); // Redireciona para a página de login se o usuário não estiver logado
            return mv;
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
        
        mv.addObject("usuario", usuario); // Adiciona o usuário ao modelo

        // Lista de garanhões para exibir no formulário
        mv.addObject("listarGaranhoes", garanhaoRepositorio.findAll());

        // Adiciona o objeto de movimentação para o formulário
        mv.addObject("movimentacao", movimentacao);

        return mv;
    }

    // Listagem de todas as movimentações
    @GetMapping("/administrativo/movimentacoes/listarData")
    public ModelAndView listarMovimentacoesData(HttpSession session,  Model model) {
        ModelAndView mv = new ModelAndView("administrativo/movimentacoes/listaData");

        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            // Redireciona para o login se o usuário não estiver logado
            mv.setViewName("redirect:/login");
            return mv;
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
        
        mv.addObject("usuario", usuario); // Adiciona o usuário ao modelo

        // Lista de movimentações para exibir na página
        List<Movimentacao> listaMovimentacoes = movimentacaoRepositorio.findAll();
        mv.addObject("listaMovimentacoes", listaMovimentacoes);

        return mv;
    }
    
    // Listagem de todas as movimentações
    @GetMapping("/administrativo/movimentacoes/listarGaranhoes")
    public ModelAndView listarMovimentacoesGaranhoes(HttpSession session,  Model model) {
        ModelAndView mv = new ModelAndView("administrativo/movimentacoes/listaGaranhoes");

        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            // Redireciona para o login se o usuário não estiver logado
            mv.setViewName("redirect:/login");
            return mv;
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
        
        mv.addObject("usuario", usuario); // Adiciona o usuário ao modelo

        // Lista de movimentações para exibir na página
        List<Movimentacao> listaMovimentacoes = movimentacaoRepositorio.findAll();
        mv.addObject("listaMovimentacoes", listaMovimentacoes);

        return mv;
    }
    
    // Listagem de todas as movimentações
    @GetMapping("/administrativo/movimentacoes/listarDestinos")
    public ModelAndView listarMovimentacoesDestinos(HttpSession session,  Model model) {
        ModelAndView mv = new ModelAndView("administrativo/movimentacoes/listaDestinos");

        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            // Redireciona para o login se o usuário não estiver logado
            mv.setViewName("redirect:/login");
            return mv;
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
        
        mv.addObject("usuario", usuario); // Adiciona o usuário ao modelo

        // Lista de movimentações para exibir na página
        List<Movimentacao> listaMovimentacoes = movimentacaoRepositorio.findAll();
        mv.addObject("listaMovimentacoes", listaMovimentacoes);

        return mv;
    }
    
    // Listagem de todas as movimentações
    @GetMapping("/administrativo/movimentacoes/listarProfissionais")
    public ModelAndView listarMovimentacoesProfissionais(HttpSession session,  Model model) {
        ModelAndView mv = new ModelAndView("administrativo/movimentacoes/listaProfissionais");

        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            // Redireciona para o login se o usuário não estiver logado
            mv.setViewName("redirect:/login");
            return mv;
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
        
        mv.addObject("usuario", usuario); // Adiciona o usuário ao modelo

        // Lista de movimentações para exibir na página
        List<Movimentacao> listaMovimentacoes = movimentacaoRepositorio.findAll();
        mv.addObject("listaMovimentacoes", listaMovimentacoes);

        return mv;
    }
 
 // Página de edição de movimentação
    @GetMapping("/administrativo/movimentacoes/eventoMovimentacao/editarMovimentacao/{id_movimentacao}")
    public String editar(@PathVariable("id_movimentacao") Long id_movimentacao, Model model, HttpSession session) {
        // Verifica se o usuário está logado
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        if (usuario == null) {
            return "redirect:/login"; // Redireciona para o login se o usuário não estiver logado
        }

        System.out.println("Hora Fim do Usuário: " + usuario.getHoraFim()); // Verifica se o valor existe

        if (usuario.getHoraFim() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            model.addAttribute("horaFim", usuario.getHoraFim().format(formatter));
        } else {
            model.addAttribute("horaFim", ""); // Evita erro caso seja null
        }

        // Adiciona o usuário atual ao modelo (opcional, caso precise exibir informações do usuário na página)
        model.addAttribute("usuario", usuario); 

        // Busca a movimentação pelo ID
        Optional<Movimentacao> movimentacao = movimentacaoRepositorio.findById(id_movimentacao);

        if (movimentacao.isPresent()) {
            Movimentacao movimentacaoEncontrada = movimentacao.get(); // Obtém a movimentação encontrada
            Garanhao garanhao = movimentacaoEncontrada.getGaranhao(); // Obtém o garanhão relacionado à movimentação

            // Adiciona os dados da movimentação e do garanhão ao modelo
            model.addAttribute("movimentacao", movimentacaoEncontrada);
            
            // Verifica se o garanhão existe antes de tentar acessar seus atributos
            model.addAttribute("nome_garanhao", garanhao != null ? garanhao.getNome_garanhao() : "N/A");
            model.addAttribute("saldo_atual_palhetas", garanhao != null ? garanhao.getSaldo_atual_palhetas() : 0);
            
            // Adiciona o endereço da movimentação ao modelo
            model.addAttribute("endereco", movimentacaoEncontrada.getEndereco());

            // Adiciona o nome do usuário responsável pela movimentação
            model.addAttribute("nome_usuario_responsavel", movimentacaoEncontrada.getNome_usuario());

            return "administrativo/movimentacoes/eventoMovimentacao"; // Retorna para a página de edição da movimentação
        }

        // Caso não encontre a movimentação, redireciona para a lista de movimentações
        return "redirect:/administrativo/movimentacoes/listarData";
    }

    
    @PostMapping("/removerMovimentacao/{id_movimentacao}")
    public String remover(@PathVariable("id_movimentacao") Long idMovimentacao,
                          @RequestParam("motivoExclusao") String motivoExclusao, 
                          Model model, HttpSession session) {

        // Verifica se o usuário está logado na sessão
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login";
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
        
        model.addAttribute("usuario", usuario);

        try {
            // Busca a movimentação pelo ID
            Optional<Movimentacao> movimentacaoOptional = movimentacaoRepositorio.findById(idMovimentacao);
            if (movimentacaoOptional.isPresent()) {
                Movimentacao movimentacao = movimentacaoOptional.get();

                System.out.println("USUÁRIO EXCLUIU UMA MOVIMENTAÇÃO");
                System.out.println("MOVIMENTACÃO EXCLUIDA:");
                System.out.println("Nome Garanhão: " + movimentacao.getNome_garanhao());
                System.out.println("Botijão: " + movimentacao.getBotijao());
                System.out.println("Caneca: " + movimentacao.getCaneca());
                System.out.println("Palheta: " + movimentacao.getCor_palheta());
                System.out.println("Quantidade Movimentada: " + movimentacao.getQuantidade());
                System.out.println("Destino: " + movimentacao.getDestino());
                System.out.println("Endereco: " + movimentacao.getEndereco());
                System.out.println("Embriao: " + movimentacao.getEmbriao());
                System.out.println("Profissional: " + movimentacao.getProfissional());
                System.out.println("Nome: " + movimentacao.getNome_profissional());
                System.out.println("Documento: " + movimentacao.getIdentificador_profissional());
                System.out.println("Data da movimentação: " + movimentacao.getData_movimentacao());

                // Obtemos o nome do garanhão e a quantidade de palhetas antes da exclusão
                String nomeGaranhao = movimentacao.getNome_garanhao();
                int quantidadePalhetasRemovidas = movimentacao.getQuantidade();

                // Zera a quantidade de palhetas na movimentação
                movimentacao.setQuantidade(0);  // Zera a quantidade de palhetas
                movimentacaoRepositorio.save(movimentacao);  // Salva a movimentação com a quantidade zerada

                // Atualiza o saldo de palhetas do garanhão associado
                Garanhao garanhao = movimentacao.getGaranhao();  // Obtém o Garanhão associado à movimentação

                // Exibe os dados do garanhão antes de ajustar o saldo
                System.out.println("Saldo anterior de palhetas do Garanhão: " + garanhao.getSaldo_atual_palhetas());

                // Ajusta o saldo de palhetas do garanhão (somando a quantidade de volta)
                garanhao.ajustarSaldoAtual(quantidadePalhetasRemovidas);  // Adiciona de volta ao saldo do garanhão
                garanhaoRepositorio.save(garanhao);  // Salva o garanhão com o saldo atualizado

                // Exibe o novo saldo de palhetas do garanhão após o ajuste
                System.out.println("Novo saldo de palhetas do Garanhão: " + garanhao.getSaldo_atual_palhetas());

                // Cria uma entrada na lixeira para armazenar a exclusão
                Lixeira lixeira = new Lixeira(
                        movimentacao.getId_movimentacao(), 
                        motivoExclusao, 
                        nomeGaranhao,
                        quantidadePalhetasRemovidas,
                        usuario.getNome_usuario() // Nome do usuário responsável correto
                );

                lixeiraRepositorio.save(lixeira);

                // Exclui a movimentação
                movimentacaoRepositorio.deleteById(idMovimentacao);

                // Passa a movimentação para o modelo
                model.addAttribute("remover", movimentacao);  // Adiciona o nome do garanhão à variável "remover" no modelo

                // Adiciona a mensagem de sucesso
                model.addAttribute("message", "Movimentação excluída com sucesso e quantidade de palhetas atualizada!");
                System.out.println("STATUS DA EXCLUSÃO");
                System.out.println("Movimentação excluída com sucesso e quantidade de palhetas atualizada!");
                return "administrativo/movimentacoes/remover";
            } else {
                model.addAttribute("message", "Movimentação não encontrada!");
                System.out.println("Movimentação não encontrada!");
                return "administrativo/movimentacoes/remover";
            }
        } catch (Exception e) {
            model.addAttribute("message", "Erro ao remover a movimentação: " + e.getMessage());
            System.out.println("Erro ao remover a movimentação: " + e.getMessage());
            return "administrativo/movimentacoes/remover";
        }
    }


    /*@GetMapping("/removerMovimentacao/{id_movimentacao}")
    public String remover(@PathVariable("id_movimentacao") Long id_movimentacao, Model model, HttpSession session) {
        System.out.println("ID recebido para exclusão: " + id_movimentacao); // Log para verificar o ID

        // Verifica se o usuário está logado na sessão
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login"; // Redireciona para login se o usuário não estiver logado
        }

        // Obtém o usuário logado da sessão
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        model.addAttribute("usuario", usuario);

        try {
            // Busca o movimentação pelo ID
            Optional<Movimentacao> movimentacaoOptional = movimentacaoRepositorio.findById(id_movimentacao);
            if (movimentacaoOptional.isPresent()) {
                Movimentacao movimentacao = movimentacaoOptional.get(); // Obtém a movimentação
                model.addAttribute("remover", movimentacao); // Envia o objeto movimentação para o modelo
                //tenta excluir o Movimentação
                movimentacaoRepositorio.deleteById(id_movimentacao);
                model.addAttribute("message", "Movimentação removida com sucesso!");
                return "administrativo/movimentacoes/remover";
            } else {
                // Caso o Movimentação não seja encontrado
                model.addAttribute("message", "Movimentação não encontrado!");
                return "administrativo/movimentacoes/remover";
            }
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("message", "Erro ao excluir: Movimentação está vinculado a outras entidades.");
            return "administrativo/movimentacoes/remover";
        } catch (Exception e) {
            System.out.println("Erro ao excluir a Movimentação: " + e.getMessage());
            model.addAttribute("message", "Erro ao excluir: " + e.getMessage());
            return "administrativo/movimentacoes/remover";
        }
    }*/


    @GetMapping("/administrativo/movimentacoes/lixeira")
    public ModelAndView listarLixeira(HttpSession session, Model model) {
        ModelAndView mv = new ModelAndView("administrativo/movimentacoes/lixeira");

        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            mv.setViewName("redirect:/login");
            return mv;
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
        
        mv.addObject("usuario", usuario);

        // Lista de itens da lixeira
        List<Lixeira> lixeiraList = lixeiraRepositorio.findAll();
        mv.addObject("lixeiraList", lixeiraList); // Passa a lista com o nome correto

        return mv;
    }
    
    /*@PostMapping("/administrativo/movimentacoes/lixeira/remover/{id}")
    public String removerMovimentacaoDaLixeira(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Lixeira> lixeira = lixeiraRepositorio.findById(id);
            
            if (lixeira.isPresent()) {
                // Remove a movimentação da lixeira
                lixeiraRepositorio.delete(lixeira.get());
                // Adiciona mensagem de sucesso
                redirectAttributes.addFlashAttribute("mensagem", "Movimentação removida com sucesso!");
            } else {
                // Caso não encontre a movimentação
                redirectAttributes.addFlashAttribute("mensagem", "Movimentação não encontrada!");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagem", "Erro ao tentar remover a movimentação.");
            e.printStackTrace(); // Verifique se há algum erro específico
        }
        // Redireciona para a lista da lixeira
        return "redirect:/administrativo/movimentacoes/lixeira";
    }*/

    @PostMapping("/salvarMovimentacao")
    public ModelAndView salvarMovimentacao(Movimentacao movimentacao) {
        // Buscar o garanhão pelo ID
        Garanhao garanhao = garanhaoRepositorio.findById(movimentacao.getGaranhao().getId_garanhao())
                .orElseThrow(() -> new RuntimeException("Garanhão não encontrado com o ID " + movimentacao.getGaranhao().getId_garanhao()));

        // Verificar se a movimentação é válida
        if (movimentacao.getQuantidade() > 0) {
            int saldoAtual = garanhao.getSaldo_atual_palhetas();
            int novaQuantidade = saldoAtual - movimentacao.getQuantidade();

            if (novaQuantidade < 0) {
                throw new RuntimeException("Saldo insuficiente de palhetas para realizar a movimentação.");
            }

            // Atualizar o saldo do garanhão
            garanhao.setSaldo_atual_palhetas(novaQuantidade);
            garanhaoRepositorio.save(garanhao);
        }

        // Preencher os novos campos na movimentação
        movimentacao.setNome_garanhao(garanhao.getNome_garanhao());

        // ✅ Agora a data só será definida como hoje se o usuário não preencher outra no formulário
        if (movimentacao.getData_movimentacao() == null) {
            movimentacao.setData_movimentacao(LocalDate.now());
        }

        // Garantir que apenas o destino selecionado pelo usuário seja salvo
        String destinoSelecionado = movimentacao.getDestino();
        if (destinoSelecionado != null && !destinoSelecionado.trim().isEmpty()) {
            movimentacao.setDestino(destinoSelecionado.trim());
        } else {
            movimentacao.setDestino(null);
        }

        // Salvar movimentação no banco
        movimentacaoRepositorio.save(movimentacao);

        // 🖨️ Exibir os valores no terminal
        System.out.println("USUÁRIO FEZ UMA MOVIMENTAÇÃO");
        System.out.println("Movimentação de Palhetas:");
        System.out.println("Nome Garanhão: " + garanhao.getNome_garanhao());
        System.out.println("Botijão: " + garanhao.getBotijao());
        System.out.println("Caneca: " + garanhao.getCaneca());
        System.out.println("Palheta: " + garanhao.getCor_palheta());
        System.out.println("Quantidade Movimentada: " + movimentacao.getQuantidade());
        System.out.println("Destino: " + movimentacao.getDestino()); 
        System.out.println("Endereco: " + movimentacao.getEndereco()); 
        System.out.println("Satus do Embrião: " + movimentacao.getEmbriao());
        System.out.println("Quantidade de Embrião: " + movimentacao.getQuantidade_embriao());
        System.out.println("Profissional: " + movimentacao.getProfissional());
        System.out.println("Nome: " + movimentacao.getNome_profissional());
        System.out.println("Documento: " + movimentacao.getIdentificador_profissional());
        System.out.println("Data da movimentação: " + movimentacao.getData_movimentacao());

        return new ModelAndView("redirect:/administrativo/movimentacoes/listarData");
    }
    
    @Transactional
    @PostMapping("/administrativo/movimentacoes/editarMovimentacao")
    public String salvarEdicaoMovimentacao(@ModelAttribute("movimentacao") Movimentacao movimentacao, RedirectAttributes redirectAttributes, HttpSession session) {
        try {
            // Verifica se o usuário está logado
            Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
            if (usuario == null) {
                return "redirect:/login";
            }

            // Buscar a movimentação existente pelo ID
            Optional<Movimentacao> movimentacaoExistenteOpt = movimentacaoRepositorio.findById(movimentacao.getId_movimentacao());
            
            if (movimentacaoExistenteOpt.isPresent()) {
                Movimentacao movimentacaoExistente = movimentacaoExistenteOpt.get();

                // Exibir os dados antigos antes da alteração
                System.out.println("USUÁRIO FEZ UMA EDIÇÃO NA MOVIMENTAÇÃO");
                System.out.println("Dados Antigos da Movimentação:");
                System.out.println("Nome Garanhão: " + movimentacaoExistente.getGaranhao().getNome_garanhao());
                System.out.println("Botijão: " + movimentacaoExistente.getGaranhao().getBotijao());
                System.out.println("Caneca: " + movimentacaoExistente.getGaranhao().getCaneca());
                System.out.println("Palheta: " + movimentacaoExistente.getGaranhao().getCor_palheta());
                System.out.println("Quantidade Movimentada: " + movimentacaoExistente.getQuantidade());
                System.out.println("Destino: " + movimentacaoExistente.getDestino());
                System.out.println("Endereco: " + movimentacaoExistente.getEndereco());
                System.out.println("Status do embrião: " + movimentacaoExistente.getEmbriao());
                System.out.println("Quantidade de Embrião: " + movimentacaoExistente.getQuantidade_embriao());
                System.out.println("Profissional: " + movimentacaoExistente.getNome_profissional());
                System.out.println("Documento: " + movimentacaoExistente.getIdentificador_profissional());
                System.out.println("Data da movimentação: " + movimentacaoExistente.getData_movimentacao());

                // Obter o garanhão associado à movimentação
                Garanhao garanhao = movimentacaoExistente.getGaranhao();
                if (garanhao == null) {
                    redirectAttributes.addFlashAttribute("mensagemErro", "Garanhão associado à movimentação não encontrado.");
                    System.out.println("Garanhão associado à movimentação não encontrado.");
                    return "redirect:/administrativo/movimentacoes/listarData";
                }

                // Calcular a diferença entre a quantidade antiga e a nova
                int quantidadeAntiga = movimentacaoExistente.getQuantidade();
                int diferenca = movimentacao.getQuantidade() - quantidadeAntiga;

                // Atualizar o saldo do garanhão
                int novoSaldo = garanhao.getSaldo_atual_palhetas() - diferenca;

                if (novoSaldo < 0) {
                    // Se o saldo for insuficiente
                    redirectAttributes.addFlashAttribute("mensagemErro", "Saldo insuficiente para ajustar a movimentação.");
                    System.out.println("Saldo insuficiente para ajustar a movimentação.");
                    return "redirect:/administrativo/movimentacoes/listarData";
                }

                garanhao.setSaldo_atual_palhetas(novoSaldo);

                // Atualizar os campos editáveis da movimentação
                movimentacaoExistente.setNome_garanhao(movimentacao.getNome_garanhao());
                movimentacaoExistente.setQuantidade(movimentacao.getQuantidade());
                movimentacaoExistente.setEndereco(movimentacao.getEndereco());
                movimentacaoExistente.setIdentificador_profissional(movimentacao.getIdentificador_profissional());
                movimentacaoExistente.setNome_profissional(movimentacao.getNome_profissional());
                movimentacaoExistente.setEmbriao(movimentacao.getEmbriao());
                movimentacaoExistente.setQuantidade_embriao(movimentacao.getQuantidade_embriao());
                movimentacaoExistente.setData_movimentacao(movimentacao.getData_movimentacao());

                
                // Atualiza o destino apenas se o usuário for ADMIN
                if (usuario.getTipo().equals("ADMIN")) {
                    movimentacaoExistente.setDestino(movimentacao.getDestino());
                }

                // Exibir os novos dados após a alteração
                System.out.println("Novos Dados da Movimentação:");
                System.out.println("Nome Garanhão: " + movimentacaoExistente.getGaranhao().getNome_garanhao());
                System.out.println("Botijão: " + movimentacaoExistente.getGaranhao().getBotijao());
                System.out.println("Caneca: " + movimentacaoExistente.getGaranhao().getCaneca());
                System.out.println("Palheta: " + movimentacaoExistente.getGaranhao().getCor_palheta());
                System.out.println("Quantidade Movimentada: " + movimentacaoExistente.getQuantidade());
                System.out.println("Destino: " + movimentacaoExistente.getDestino());
                System.out.println("Endereco: " + movimentacaoExistente.getEndereco());
                System.out.println("Status do embrião: " + movimentacaoExistente.getEmbriao());
                System.out.println("Quantidade de Embrião: " + movimentacaoExistente.getQuantidade_embriao());
                System.out.println("Profissional: " + movimentacaoExistente.getNome_profissional());
                System.out.println("Documento: " + movimentacaoExistente.getIdentificador_profissional());
                System.out.println("Data da movimentação: " + movimentacaoExistente.getData_movimentacao());

                // Salvar as atualizações no banco de dados
                movimentacaoRepositorio.save(movimentacaoExistente);
                garanhaoRepositorio.save(garanhao);

                // Mensagem de sucesso
                redirectAttributes.addFlashAttribute("mensagemSucesso", "Movimentação atualizada com sucesso.");
                System.out.println("Movimentação atualizada com sucesso.");
                return "redirect:/administrativo/movimentacoes/listarData"; // Redireciona para a lista de movimentações
            } else {
                // Caso a movimentação não seja encontrada
                redirectAttributes.addFlashAttribute("mensagemErro", "Movimentação não encontrada.");
                System.out.println("Movimentação não encontrada.");
                return "redirect:/administrativo/movimentacoes/listarData";
            }
        } catch (Exception e) {
            System.out.println("Erro ao salvar movimentação: " + e.getMessage());
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar movimentação.");
            System.out.println("Erro ao salvar movimentação.");
            return "redirect:/administrativo/movimentacoes/listarData";
        }
    }
 
}
