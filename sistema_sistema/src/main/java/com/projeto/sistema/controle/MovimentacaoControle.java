package com.projeto.sistema.controle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import com.projeto.sistema.modelos.Garanhao;
import com.projeto.sistema.modelos.Movimentacao;
import com.projeto.sistema.modelos.Usuario;
import com.projeto.sistema.repositorios.GaranhaoRepositorio;
import com.projeto.sistema.repositorios.MovimentacaoRepositorio;
import jakarta.servlet.http.HttpSession;

@Controller
public class MovimentacaoControle {

    @Autowired
    private MovimentacaoRepositorio movimentacaoRepositorio;

    @Autowired
    private GaranhaoRepositorio garanhaoRepositorio;

    // Página de cadastro de movimentação
    @GetMapping("/administrativo/movimentacoes/cadastro")
    public ModelAndView cadastrar(Movimentacao movimentacao, HttpSession session) {
        ModelAndView mv = new ModelAndView("/administrativo/movimentacoes/cadastro");

        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            mv.setViewName("redirect:/login"); // Redireciona para a página de login se o usuário não estiver logado
            return mv;
        }

        // Obtém o usuário logado da sessão
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
        mv.addObject("usuario", usuario); // Adiciona o usuário ao modelo

        // Lista de garanhões para exibir no formulário
        mv.addObject("listarGaranhoes", garanhaoRepositorio.findAll());

        // Adiciona o objeto de movimentação para o formulário
        mv.addObject("movimentacao", movimentacao);

        return mv;
    }

    // Listagem de todas as movimentações
    @GetMapping("/administrativo/movimentacoes/listar")
    public ModelAndView listarMovimentacoes(HttpSession session) {
        ModelAndView mv = new ModelAndView("administrativo/movimentacoes/lista");

        // Verifica se o usuário está logado
        if (session.getAttribute("usuarioLogado") == null) {
            // Redireciona para o login se o usuário não estiver logado
            mv.setViewName("redirect:/login");
            return mv;
        }

        // Obtém o usuário logado da sessão
        Usuario usuario = (Usuario) session.getAttribute("usuarioLogado");
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

        model.addAttribute("usuario", usuario); // Adiciona o usuário atual ao modelo (opcional)

        // Busca a movimentação pelo ID
        Optional<Movimentacao> movimentacao = movimentacaoRepositorio.findById(id_movimentacao);

        if (movimentacao.isPresent()) {
            Movimentacao movimentacaoEncontrada = movimentacao.get();
            Garanhao garanhao = movimentacaoEncontrada.getGaranhao();

            // Adiciona os dados da movimentação ao modelo
            model.addAttribute("movimentacao", movimentacaoEncontrada);
            model.addAttribute("nome_garanhao", garanhao != null ? garanhao.getNome_garanhao() : "N/A");
            model.addAttribute("saldo_atual_palhetas", garanhao != null ? garanhao.getSaldo_atual_palhetas() : 0);
            model.addAttribute("endereco", movimentacaoEncontrada.getEndereco());

            model.addAttribute("nome_usuario_responsavel", movimentacaoEncontrada.getNome_usuario());

            return "administrativo/movimentacoes/eventoMovimentacao"; // Retorna para a página de edição
        }

        // Caso não encontre a movimentação, redireciona para a lista de movimentações
        return "redirect:/administrativo/movimentacoes/listar";
    }
    
    @GetMapping("/removerMovimentacao/{id_movimentacao}")
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
    }

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
            garanhaoRepositorio.save(garanhao); // Salvar a atualização no saldo do garanhão
        }

        // Preencher os novos campos na movimentação
        movimentacao.setNome_garanhao(garanhao.getNome_garanhao());
        movimentacao.setData_movimentacao(LocalDateTime.now());

        // Garantir que apenas o destino selecionado pelo usuário seja salvo
        String destinoSelecionado = movimentacao.getDestino();
        if (destinoSelecionado != null && !destinoSelecionado.trim().isEmpty()) {
            movimentacao.setDestino(destinoSelecionado.trim()); // Remove espaços e salva apenas o destino válido
        } else {
            movimentacao.setDestino(null); // Se nenhum destino for selecionado, define como null
        }

        // Salvar movimentação no banco
        movimentacaoRepositorio.save(movimentacao);

        // Redirecionar para a lista de movimentações
        return new ModelAndView("redirect:/administrativo/movimentacoes/listar");
    }
    
    @PostMapping("/administrativo/movimentacoes/editarMovimentacao")
    public String salvarEdicaoMovimentacao(@ModelAttribute("movimentacao") Movimentacao movimentacao, RedirectAttributes redirectAttributes) {
        // Buscar a movimentação existente pelo ID
        Optional<Movimentacao> movimentacaoExistenteOpt = movimentacaoRepositorio.findById(movimentacao.getId_movimentacao());
        
        if (movimentacaoExistenteOpt.isPresent()) {
            Movimentacao movimentacaoExistente = movimentacaoExistenteOpt.get();

            // Obter o garanhão associado à movimentação
            Garanhao garanhao = movimentacaoExistente.getGaranhao();
            if (garanhao == null) {
                redirectAttributes.addFlashAttribute("mensagemErro", "Garanhão associado à movimentação não encontrado.");
                return "redirect:/administrativo/movimentacoes/listar";
            }

            // Calcular a diferença entre a quantidade antiga e a nova
            int quantidadeAntiga = movimentacaoExistente.getQuantidade();
            int diferenca = movimentacao.getQuantidade() - quantidadeAntiga;

            // Atualizar o saldo do garanhão
            int novoSaldo = garanhao.getSaldo_atual_palhetas() - diferenca;

            if (novoSaldo < 0) {
                // Se o saldo for insuficiente
                redirectAttributes.addFlashAttribute("mensagemErro", "Saldo insuficiente para ajustar a movimentação.");
                return "redirect:/administrativo/movimentacoes/listar";
            }

            garanhao.setSaldo_atual_palhetas(novoSaldo);

            // Atualizar os campos editáveis da movimentação
            movimentacaoExistente.setNome_garanhao(movimentacao.getNome_garanhao());
            movimentacaoExistente.setQuantidade(movimentacao.getQuantidade());
            movimentacaoExistente.setDestino(movimentacao.getDestino());
            movimentacaoExistente.setEndereco(movimentacao.getEndereco());
            movimentacaoExistente.setIdentificador_profissional(movimentacao.getIdentificador_profissional());
            movimentacaoExistente.setNome_profissional(movimentacao.getNome_profissional());
            // Salvar as atualizações no banco de dados
            movimentacaoRepositorio.save(movimentacaoExistente);
            garanhaoRepositorio.save(garanhao);

            // Mensagem de sucesso
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Movimentação atualizada com sucesso.");
            return "redirect:/administrativo/movimentacoes/listar"; // Redireciona para a lista de movimentações
        } else {
            // Caso a movimentação não seja encontrada
            redirectAttributes.addFlashAttribute("mensagemErro", "Movimentação não encontrada.");
            return "redirect:/administrativo/movimentacoes/listar";
        }

    }
 
}