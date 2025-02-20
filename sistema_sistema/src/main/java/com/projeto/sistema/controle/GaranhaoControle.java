package com.projeto.sistema.controle;

import java.time.LocalDateTime;
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
    public ModelAndView cadastrar(HttpSession session, Garanhao garanhao) {
        ModelAndView mv = new ModelAndView("/administrativo/garanhoes/cadastro");

        // Verifica se o usuário está logado
        if (session == null || session.getAttribute("usuarioLogado") == null) {
            mv.setViewName("redirect:/login"); // Redireciona para login se o usuário não estiver logado
            return mv;
        }

        // Obtém o usuário logado da sessão
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

        // Adiciona o objeto 'usuario' ao modelo
        mv.addObject("usuario", usuario);
        mv.addObject("garanhao", garanhao);  // Adiciona o objeto 'garanhao' ao modelo

        return mv;
    }



    @GetMapping("/administrativo/garanhoes/listar")
    public ModelAndView listarGaranhoes(HttpSession session) {
        ModelAndView mv = new ModelAndView("administrativo/garanhoes/lista");

        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            mv.setViewName("redirect:/login"); // Redireciona para login se o usuário não estiver logado
            return mv;
        }

        // Obtém o usuário logado da sessão
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");

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
        return "redirect:/administrativo/garanhoes/listar";
    }
    
    @PostMapping("/administrativo/garanhoes/editarGaranhao")
    public String salvarEdicaoGaranhao(@ModelAttribute("garanhao") Garanhao garanhao, RedirectAttributes redirectAttributes) {
        // Buscar o garanhão existente pelo ID
        Optional<Garanhao> garanhaoExistenteOpt = garanhaoRepositorio.findById(garanhao.getId_garanhao());

        if (garanhaoExistenteOpt.isPresent()) {
            Garanhao garanhaoExistente = garanhaoExistenteOpt.get();

            // Garantir que o saldo inicial não seja negativo
            if (garanhao.getSaldo_inicial_palhetas() < 0) {
                redirectAttributes.addFlashAttribute("mensagemErro", "O saldo de palhetas não pode ser negativo.");
                return "redirect:/administrativo/garanhoes/listar";
            }

            // Atualizar apenas os campos alterados
            atualizarCamposGaranhao(garanhao, garanhaoExistente);

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

    // Método auxiliar para atualizar os campos
    private void atualizarCamposGaranhao(Garanhao novo, Garanhao existente) {
        // Verifica e atualiza o saldo inicial e ajusta o saldo atual
        int saldoInicialAnterior = existente.getSaldo_inicial_palhetas();
        int saldoAtualAnterior = existente.getSaldo_atual_palhetas();

        existente.setSaldo_inicial_palhetas(novo.getSaldo_inicial_palhetas());
        if (saldoInicialAnterior != novo.getSaldo_inicial_palhetas()) {
            int diferenca = novo.getSaldo_inicial_palhetas() - saldoInicialAnterior;
            existente.setSaldo_atual_palhetas(saldoAtualAnterior + diferenca);
        }

        // Atualizar outros campos
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

        // Atualiza apenas a data de contagem inicial, se necessária
        if (novo.getData_contagem_inicial() != null) {
            existente.setData_contagem_inicial(novo.getData_contagem_inicial());
        }
    }

    @PostMapping("/administrativo/garanhoes/salvar")
    public ModelAndView salvar(@ModelAttribute Garanhao garanhao, BindingResult result) {
        // Validação do formulário
        if (result.hasErrors()) {
            // Se houver erros de validação, volta para o formulário com os erros
            return cadastrar(null, garanhao);  // Retorna para o formulário com os erros
        }

        // Definir a data de cadastro (utiliza a data e hora atuais)
        garanhao.setData_cadastro(LocalDateTime.now());

        // Verificar o saldo inicial de palhetas e definir o saldo atual
        if (garanhao.getSaldo_inicial_palhetas() > 0) {
            garanhao.setSaldo_atual_palhetas(garanhao.getSaldo_inicial_palhetas());
        } else {
            // Caso o saldo inicial seja 0 ou negativo, o saldo atual também será 0
            garanhao.setSaldo_atual_palhetas(0);
        }

        try {
            // Salvando o Garanhão
            garanhaoRepositorio.save(garanhao);  // Salva sem precisar de flush, o Spring cuida disso
        } catch (Exception e) {
            // Caso ocorra algum erro ao salvar, você pode capturar e tratar aqui
            e.printStackTrace();  // Exibe o erro detalhado no log para diagnóstico
            result.rejectValue("nome_garanhao", "error.garanhao", "Erro ao salvar o Garanhão. Tente novamente.");
            return cadastrar(null, garanhao);  // Retorna para o formulário com a mensagem de erro
        }

        // Após salvar com sucesso, redireciona para a listagem dos Garanhões
        return new ModelAndView("redirect:/administrativo/garanhoes/listar");  // Redireciona para a listagem após salvar
    }

    @GetMapping("/removerGaranhao/{id_garanhao}")
    public String remover(@PathVariable("id_garanhao") Long id_garanhao, Model model, HttpSession session) {
        System.out.println("ID recebido para exclusão: " + id_garanhao); // Log para verificar o ID

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

                // Verifica se há movimentações associadas ao garanhão
                List<Movimentacao> movimentacoesAssociadas = movimentacaoRepositorio.findAll()
                        .stream()
                        .filter(movimentacao -> movimentacao.getGaranhao().getId_garanhao().equals(id_garanhao))
                        .collect(Collectors.toList());

                if (!movimentacoesAssociadas.isEmpty()) {
                    // Caso existam movimentações associadas
                    model.addAttribute("message", "Erro ao excluir: Garanhão possui movimentações associadas.");
                    return "administrativo/garanhoes/remover";
                }

                // Se não houver movimentações, tenta excluir o garanhão
                garanhaoRepositorio.deleteById(id_garanhao);
                model.addAttribute("message", "Garanhão removido com sucesso!");
                return "administrativo/garanhoes/remover";
            } else {
                // Caso o garanhão não seja encontrado
                model.addAttribute("message", "Garanhão não encontrado!");
                return "administrativo/garanhoes/remover";
            }
        } catch (DataIntegrityViolationException e) {
            model.addAttribute("message", "Erro ao excluir: Garanhão está vinculado a outras entidades.");
            return "administrativo/garanhoes/remover";
        } catch (Exception e) {
            System.out.println("Erro ao excluir o garanhão: " + e.getMessage());
            model.addAttribute("message", "Erro ao excluir: " + e.getMessage());
            return "administrativo/garanhoes/remover";
        }
    }

    @PostMapping("/administrativo/garanhoes/ajustarSaldo")
    public ModelAndView ajustarSaldo(Long idGaranhao, int quantidade) {
        if (quantidade <= 0) {
            // Caso a quantidade seja inválida
            return listarGaranhoes(null);
        }

        Optional<Garanhao> garanhaoOpt = garanhaoRepositorio.findById(idGaranhao);

        if (garanhaoOpt.isPresent()) {
            Garanhao garanhao = garanhaoOpt.get();
            int novoSaldo = garanhao.getSaldo_atual_palhetas() - quantidade;

            if (novoSaldo < 0) {
                // Se o saldo for insuficiente, retorne uma mensagem de erro
                return listarGaranhoes(null);
            }

            // Atualiza o saldo e salva o Garanhao
            garanhao.setSaldo_atual_palhetas(novoSaldo);
            garanhaoRepositorio.save(garanhao);
        } else {
            // Se o Garanhão não for encontrado, retorne um erro ou mensagem
        }

        return listarGaranhoes(null);
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

}