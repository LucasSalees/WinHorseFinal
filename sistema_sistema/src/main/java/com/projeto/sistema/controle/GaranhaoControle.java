package com.projeto.sistema.controle;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.projeto.sistema.modelos.Garanhao;
import com.projeto.sistema.modelos.Movimentacao;
import com.projeto.sistema.modelos.Usuario;
import com.projeto.sistema.repositorios.GaranhaoRepositorio;
import com.projeto.sistema.repositorios.MovimentacaoRepositorio;

import jakarta.servlet.http.HttpSession;

@Controller
public class GaranhaoControle {

    @Autowired
    private GaranhaoRepositorio garanhaoRepositorio;
    
    @Autowired
    private MovimentacaoRepositorio movimentacaoRepositorio;

    @GetMapping("/administrativo/garanhoes/cadastro")
    public ModelAndView cadastrar(HttpSession session, Garanhao garanhao, Model model) {
        ModelAndView mv = new ModelAndView("/administrativo/garanhoes/cadastro");

        // Verifica se o usuário está logado
        if (session == null || session.getAttribute("usuarioLogado") == null) {
            mv.setViewName("redirect:/login"); // Redireciona para login se o usuário não estiver logado
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

        // Adiciona o objeto 'usuario' ao modelo
        mv.addObject("usuario", usuario);
        mv.addObject("garanhao", garanhao);  // Adiciona o objeto 'garanhao' ao modelo

        return mv;
    }
    
    @GetMapping("/administrativo/garanhoes/listar")
    public ModelAndView listarGaranhoes(HttpSession session, Model model) {
        ModelAndView mv = new ModelAndView("administrativo/garanhoes/lista");

        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            mv.setViewName("redirect:/login"); // Redireciona para login se o usuário não estiver logado
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

        // Adiciona o objeto 'usuario' ao modelo
        mv.addObject("usuario", usuario);

        // Adiciona a lista de garanhões ao modelo
        mv.addObject("listaGaranhoes", garanhaoRepositorio.findAll());

        return mv;
    }
    
    @GetMapping("/administrativo/garanhoes/listaValor")
    public ModelAndView listarValoresGaranhoes(HttpSession session, Model model) {
        ModelAndView mv = new ModelAndView("administrativo/garanhoes/listaValor"); // Aponta para a página correta

        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            mv.setViewName("redirect:/login"); // Redireciona para login se o usuário não estiver logado
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

        // Adiciona o objeto 'usuario' ao modelo
        mv.addObject("usuario", usuario);

        // Adiciona a lista de garanhões ao modelo
        mv.addObject("listaGaranhoes", garanhaoRepositorio.findAll());

        return mv;
    }



    // Editar um garanhão específico pelo ID
    @GetMapping("/administrativo/garanhoes/eventoGaranhao/editarGaranhao/{id_garanhao}")
    public String editar(@PathVariable("id_garanhao") Long id_garanhao, Model model, HttpSession session) {
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
        
        System.out.println("Hora Fim do Usuário: " + usuario.getHoraFim()); // Verifica se o valor existe

        if (usuario.getHoraFim() != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
            model.addAttribute("horaFim", usuario.getHoraFim().format(formatter));
        } else {
            model.addAttribute("horaFim", ""); // Evita erro caso seja null
        }
        
        model.addAttribute("usuario", usuario); // Adiciona o usuário ao modelo

        // Busca o garanhão pelo id
        Optional<Garanhao> garanhao = garanhaoRepositorio.findById(id_garanhao);

        // Se o garanhão for encontrado, exibe a página de edição
        if (garanhao.isPresent()) {
            model.addAttribute("garanhao", garanhao.get()); // Adiciona o objeto garanhão ao modelo
            model.addAttribute("nome_garanhao", garanhao.get().getNome_garanhao()); // Adiciona o nome do garanhão ao modelo
            model.addAttribute("cor_palheta", garanhao.get().getCor_palheta());
            return "administrativo/garanhoes/eventoGaranhao"; // Retorna para a página de edição
        }

        // Caso não encontre o garanhão, redireciona para a lista de garanhões
        return "redirect:/administrativo/garanhoes/listaValor";
    }
    
    // Editar um garanhão específico pelo ID
    @GetMapping("/administrativo/garanhoes/eventoValor/editarValor/{id_garanhao}")
    public String editarValor(@PathVariable("id_garanhao") Long id_garanhao, Model model, HttpSession session) {
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
        
        model.addAttribute("usuario", usuario); // Adiciona o usuário ao modelo

        // Busca o garanhão pelo id
        Optional<Garanhao> garanhao = garanhaoRepositorio.findById(id_garanhao);

        // Se o garanhão for encontrado, exibe a página de edição
        if (garanhao.isPresent()) {
            model.addAttribute("garanhao", garanhao.get()); // Adiciona o objeto garanhão ao modelo
            model.addAttribute("nome_garanhao", garanhao.get().getNome_garanhao()); // Adiciona o nome do garanhão ao modelo
            model.addAttribute("cor_palheta", garanhao.get().getCor_palheta());
            return "administrativo/garanhoes/eventoValor"; // Retorna para a página de edição
        }

        // Caso não encontre o garanhão, redireciona para a lista de garanhões
        return "redirect:/administrativo/garanhoes/listaValor";
    }
    
    @PostMapping("/administrativo/garanhoes/editarValor")
    public String salvarEdicaoValor(@ModelAttribute("garanhao") Garanhao garanhao, RedirectAttributes redirectAttributes) {
        // Buscar o garanhão existente pelo ID
        Optional<Garanhao> garanhaoExistenteOpt = garanhaoRepositorio.findById(garanhao.getId_garanhao());

        if (garanhaoExistenteOpt.isPresent()) {
            Garanhao garanhaoExistente = garanhaoExistenteOpt.get();

            // Exibir os dados antigos antes da alteração
            System.out.println("USUÁRIO FEZ UMA EDIÇÃO DE VALOR");
            System.out.println("Dados Antigos do Garanhão:");
            System.out.println("Nome: " + garanhaoExistente.getNome_garanhao());
            System.out.println("Valor: " + garanhaoExistente.getValor());
            System.out.println("Moeda: " + garanhaoExistente.getMoeda());
            System.out.println("MOdalidade: " + garanhaoExistente.getModalidade());

            // Garantir que o saldo atual não seja negativo
            if (garanhao.getSaldo_atual_palhetas() < 0) {
                redirectAttributes.addFlashAttribute("mensagemErro", "O saldo de palhetas não pode ser negativo.");
                return "redirect:/administrativo/garanhoes/listaValor";
            }

            // Atualizar apenas os campos alterados (neste caso, o valor)
            garanhaoExistente.setValor(garanhao.getValor());
            garanhaoExistente.setMoeda(garanhao.getMoeda());
            garanhaoExistente.setModalidade(garanhao.getModalidade());

            // Exibir os novos dados após a alteração
            System.out.println("Novos Dados do Garanhão:");
            System.out.println("Nome: " + garanhaoExistente.getNome_garanhao());
            System.out.println("Valor: " + garanhaoExistente.getValor());
            System.out.println("Modalidade: " + garanhaoExistente.getModalidade());

            // Salvar as atualizações no banco de dados
            garanhaoRepositorio.save(garanhaoExistente);

            // Mensagem de sucesso
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Valor do garanhão atualizado com sucesso.");
            return "redirect:/administrativo/garanhoes/listaValor";
        } else {
            // Caso o garanhão não seja encontrado
            redirectAttributes.addFlashAttribute("mensagemErro", "Garanhão não encontrado.");
            return "redirect:/administrativo/garanhoes/listaValor";
        }
    }


    @PostMapping("/administrativo/garanhoes/editarGaranhao")
    public String salvarEdicaoGaranhao(@ModelAttribute("garanhao") Garanhao garanhao, RedirectAttributes redirectAttributes) {
        // Buscar o garanhão existente pelo ID
        Optional<Garanhao> garanhaoExistenteOpt = garanhaoRepositorio.findById(garanhao.getId_garanhao());

        if (garanhaoExistenteOpt.isPresent()) {
            Garanhao garanhaoExistente = garanhaoExistenteOpt.get();

            // Exibir os dados antigos antes da alteração
            System.out.println("USUÁRIO FEZ UMA EDIÇÃO");
            System.out.println("Dados Antigos do Garanhão:");
            System.out.println("Nome: " + garanhaoExistente.getNome_garanhao());
            System.out.println("Cor da Palheta: " + garanhaoExistente.getCor_palheta());
            System.out.println("Botijão: " + garanhaoExistente.getBotijao());
            System.out.println("Caneca: " + garanhaoExistente.getCaneca());
            System.out.println("Saldo Atual de Palhetas: " + garanhaoExistente.getSaldo_atual_palhetas());
            System.out.println("Valor: " + garanhaoExistente.getValor());
            System.out.println("Moeda: " + garanhaoExistente.getMoeda());

            // Garantir que o saldo atual não seja negativo
            if (garanhao.getSaldo_atual_palhetas() < 0) {
                redirectAttributes.addFlashAttribute("mensagemErro", "O saldo de palhetas não pode ser negativo.");
                return "redirect:/administrativo/garanhoes/listar";
            }

            // Atualizar apenas os campos alterados
            atualizarCamposGaranhao(garanhao, garanhaoExistente);

            // Exibir os novos dados após a alteração
            System.out.println("Novos Dados do Garanhão:");
            System.out.println("Nome: " + garanhaoExistente.getNome_garanhao());
            System.out.println("Cor da Palheta: " + garanhaoExistente.getCor_palheta());
            System.out.println("Botijão: " + garanhaoExistente.getBotijao());
            System.out.println("Caneca: " + garanhaoExistente.getCaneca());
            System.out.println("Saldo Atual de Palhetas: " + garanhaoExistente.getSaldo_atual_palhetas());
            System.out.println("Valor: " + garanhaoExistente.getValor());
            System.out.println("Moeda: " + garanhaoExistente.getMoeda());

            // Salvar as atualizações no banco de dados
            garanhaoRepositorio.save(garanhaoExistente);

            // Mensagem de sucesso
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Garanhão atualizado com sucesso.");
            return "redirect:/administrativo/garanhoes/listar";
        } else {
            // Caso o garanhão não seja encontrado
            redirectAttributes.addFlashAttribute("mensagemErro", "Garanhão não encontrado.");
            return "redirect:/administrativo/garanhoes/listar";
        }
    }

    private void atualizarCamposGaranhao(Garanhao novo, Garanhao existente) {
        // Atualiza o saldo atual
        int saldoAtualAnterior = existente.getSaldo_atual_palhetas();
        existente.setSaldo_atual_palhetas(novo.getSaldo_atual_palhetas());

        // Atualiza outros campos
        if (!Objects.equals(existente.getNome_garanhao(), novo.getNome_garanhao())) {
            existente.setNome_garanhao(novo.getNome_garanhao());
        }

        if (existente.getBotijao() != novo.getBotijao()) {
            existente.setBotijao(novo.getBotijao());
        }

        if (existente.getCaneca() != novo.getCaneca()) {
            existente.setCaneca(novo.getCaneca());
        }

        if (!Objects.equals(existente.getCor_palheta(), novo.getCor_palheta())) {
            existente.setCor_palheta(novo.getCor_palheta());
        }

        // Atualiza a data de contagem inicial, se necessária
        if (novo.getData_contagem_inicial() != null) {
            existente.setData_contagem_inicial(novo.getData_contagem_inicial());
        }

        // Atualiza o valor
        if (novo.getValor() != existente.getValor()) {
            existente.setValor(novo.getValor());
        }

        // Atualiza a moeda
        if (!Objects.equals(existente.getMoeda(), novo.getMoeda())) {
            existente.setMoeda(novo.getMoeda());
        }

        // Atualiza a modalidade
        if (!Objects.equals(existente.getModalidade(), novo.getModalidade())) {
            existente.setModalidade(novo.getModalidade());
        }
        
        // Atualiza a data de cadastro (sempre define a data atual)
        existente.setData_cadastro(LocalDateTime.now());
        
    }

    @PostMapping("/administrativo/garanhoes/salvar")
    public ModelAndView salvar(@ModelAttribute Garanhao garanhao, BindingResult result) {
        // Validação do formulário
        if (result.hasErrors()) {
            return cadastrar(null, garanhao, null);
        }

        // Definir a data de cadastro (utiliza a data e hora atuais)
        garanhao.setData_cadastro(LocalDateTime.now());

        // Verificar o saldo atual de palhetas e definir o saldo atual
        if (garanhao.getSaldo_atual_palhetas() < 0) {
            result.rejectValue("saldo_atual_palhetas", "error.garanhao", "O saldo de palhetas não pode ser negativo.");
            return cadastrar(null, garanhao, null);
        }

        // 🖨️ Exibir os valores no terminal
        System.out.println("USUÁRIO FEZ UM CADASTRO DE GARANHÃO");
        System.out.println("Cadastro de Garanhão:");
        System.out.println("Nome Garanhão: " + garanhao.getNome_garanhao());
        System.out.println("Cor da Palheta: " + garanhao.getCor_palheta());
        System.out.println("Botijão: " + garanhao.getBotijao());
        System.out.println("Caneca: " + garanhao.getCaneca());
        System.out.println("Saldo Atual de Palhetas: " + garanhao.getSaldo_atual_palhetas());
        System.out.println("Contagem Inicial: " + garanhao.getData_contagem_inicial());
        System.out.println("Modalidade: " + garanhao.getModalidade());
        System.out.println("Valor: " + garanhao.getValor());
        System.out.println("Moeda: " + garanhao.getMoeda());
        System.out.println("Data Cadastro: " + garanhao.getData_cadastro());

        try {
            // Salvando o Garanhão
            garanhaoRepositorio.save(garanhao);
        } catch (Exception e) {
            e.printStackTrace();
            result.rejectValue("nome_garanhao", "error.garanhao", "Erro ao salvar o Garanhão. Tente novamente.");
            return cadastrar(null, garanhao, null);
        }

        return new ModelAndView("redirect:/administrativo/garanhoes/listar");
    }



    @PostMapping("/administrativo/garanhoes/ajustarSaldo")
    public ModelAndView ajustarSaldo(Long idGaranhao, int quantidade) {
        if (quantidade <= 0) {
            // Caso a quantidade seja inválida
            return listarGaranhoes(null, null);
        }

        Optional<Garanhao> garanhaoOpt = garanhaoRepositorio.findById(idGaranhao);

        if (garanhaoOpt.isPresent()) {
            Garanhao garanhao = garanhaoOpt.get();
            int novoSaldo = garanhao.getSaldo_atual_palhetas() - quantidade;

            if (novoSaldo < 0) {
                // Se o saldo for insuficiente, retorne uma mensagem de erro
                return listarGaranhoes(null, null);
            }

            // Atualiza o saldo e salva o Garanhao
            garanhao.setSaldo_atual_palhetas(novoSaldo);
            garanhaoRepositorio.save(garanhao);
        } else {
            // Se o Garanhão não for encontrado, retorne um erro ou mensagem
        }

        return listarGaranhoes(null, null);
    }

    @GetMapping("/administrativo/garanhoes/dados/{id}")
    @ResponseBody
    public Map<String, Object> obterDadosGaranhao(@PathVariable("id") Long id) {
        Map<String, Object> response = new HashMap<>();

        try {
            Optional<Garanhao> garanhaoOpt = garanhaoRepositorio.findById(id);
            if (garanhaoOpt.isPresent()) {
                Garanhao garanhao = garanhaoOpt.get();

                // Adicionando os dados ao mapa de resposta
                response.put("caneca", garanhao.getCaneca());
                response.put("saldo_atual_palhetas", garanhao.getSaldo_atual_palhetas());
                response.put("botijao", garanhao.getBotijao());
                response.put("cor_palheta", garanhao.getCor_palheta());
                response.put("quantidade", garanhao.getQuantidade());
                response.put("data_contagem_inicial", garanhao.getData_contagem_inicial());
                response.put("data_cadastro", garanhao.getData_cadastro());
            } else {
                response.put("error", "Garanhão não encontrado.");
            }
        } catch (Exception e) {
            response.put("error", "Erro ao buscar dados do garanhão.");
            e.printStackTrace();
        }

        return response;
    }	
    
    @GetMapping("/removerGaranhao/{id_garanhao}")
    public String remover(@PathVariable("id_garanhao") Long id_garanhao, Model model, HttpSession session) {

        // Verifica se o usuário está logado na sessão
        if (session.getAttribute("usuarioLogado") == null) {
            return "redirect:/login"; // Redireciona para login se o usuário não estiver logado
        }

        // Obtém o usuário logado da sessão
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        model.addAttribute("usuario", usuario);

        try {
            // Busca o garanhão pelo ID
            Optional<Garanhao> garanhaoOptional = garanhaoRepositorio.findById(id_garanhao);
            if (garanhaoOptional.isPresent()) {
                Garanhao garanhao = garanhaoOptional.get(); // Obtém o garanhão
                model.addAttribute("remover", garanhao); // Envia o objeto Garanhão para o modelo

                // Exibir os dados do garanhão a ser excluído
                System.out.println("USUÁRIO FEZ UMA TENTATIVA DE EXCLUSÃO DE GARANHÃO");
                System.out.println("GARANHÃO A SER EXCLUIDO:");
                System.out.println("ID do garanhão: " + garanhao.getId_garanhao());
                System.out.println("Nome Garanhão: " + garanhao.getNome_garanhao());
                System.out.println("Cor da Palheta: " + garanhao.getCor_palheta());
                System.out.println("Botijão: " + garanhao.getBotijao());
                System.out.println("Caneca: " + garanhao.getCaneca());
                System.out.println("Saldo Atual de Palhetas: " + garanhao.getSaldo_atual_palhetas());
                System.out.println("Contagem Inicial: " + garanhao.getData_contagem_inicial());
                System.out.println("Modalidade: " + garanhao.getModalidade());
                System.out.println("Valor: " + garanhao.getValor());
                System.out.println("Moeda: " + garanhao.getMoeda());
                System.out.println("Data Cadastro: " + garanhao.getData_cadastro());

                // Verifica se há movimentações associadas ao garanhão
                List<Movimentacao> movimentacoesAssociadas = movimentacaoRepositorio.findAll()
                        .stream()
                        .filter(movimentacao -> movimentacao.getGaranhao().getId_garanhao().equals(id_garanhao))
                        .collect(Collectors.toList());

                if (!movimentacoesAssociadas.isEmpty()) {
                    // Caso existam movimentações associadas
                    model.addAttribute("message", "Erro ao excluir: Garanhão possui movimentações associadas.");
                    System.out.println("STATUS DA EXCLUSÃO:");
                    System.out.println("Exclusão negada, garanhão possui movimentações associadas");
                    return "administrativo/garanhoes/remover";
                }

                // Se não houver movimentações, tenta excluir o garanhão
                garanhaoRepositorio.deleteById(id_garanhao);
                model.addAttribute("message", "Garanhão removido com sucesso!");
                System.out.println("STATUS DA EXCLUSÃO:");
                System.out.println("Exclusão confirmada");
                return "administrativo/garanhoes/remover";
            } else {
                // Caso o garanhão não seja encontrado
                model.addAttribute("message", "Garanhão não encontrado!");
                System.out.println("STATUS DA EXCLUSÃO:");
                System.out.println("ERRO, garanhão não encontrado");
                return "administrativo/garanhoes/remover";
            }
        } catch (DataIntegrityViolationException e) {
            // Caso ocorra um erro de violação de integridade no banco de dados
            model.addAttribute("message", "Erro ao excluir: Garanhão está vinculado a outras entidades.");
            System.out.println("STATUS DA EXCLUSÃO:");
            System.out.println("ERRO, Garanhão está vinculado a outras entidades.");
            return "administrativo/garanhoes/remover";
        } catch (Exception e) {
            // Caso qualquer outro erro ocorra
            System.out.println("Erro ao excluir o garanhão: " + e.getMessage());
            model.addAttribute("message", "Erro ao excluir: " + e.getMessage());
            System.out.println("STATUS DA EXCLUSÃO:");
            System.out.println("ERRO, Erro ao excluir: " + e.getMessage());
            return "administrativo/garanhoes/remover";
        }
    }


}