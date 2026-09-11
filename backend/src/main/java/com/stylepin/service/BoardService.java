package com.stylepin.service;
import com.stylepin.dto.*;
import com.stylepin.entity.*;
import com.stylepin.repository.*;
import com.stylepin.exception.ResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
@Service
public class BoardService {
    private final BoardRepository boards;
    private final BoardOutfitRepository members;
    private final OutfitRepository outfits;
    private final CurrentUserService current;
    private final OutfitReadService reads;
    public BoardService(BoardRepository boards,BoardOutfitRepository members,OutfitRepository outfits,CurrentUserService current,OutfitReadService reads){
        this.boards=boards;this.members=members;this.outfits=outfits;this.current=current;this.reads=reads;
    }
    private Board owned(Long id,boolean lock){
        Board board=(lock?boards.lockById(id):boards.findById(id)).orElseThrow(()->new ResourceNotFoundException("Board not found"));
        if(!board.getOwner().getId().equals(current.get().getId()))throw new AccessDeniedException("Board is private");
        return board;
    }
    @Transactional public BoardResponseDTO create(BoardRequestDTO request){
        return BoardResponseDTO.from(boards.save(new Board(current.get(),request.name().trim(),request.description()==null?null:request.description().trim())));
    }
    @Transactional(readOnly=true) public PageResponseDTO<BoardResponseDTO> list(int page,int size){
        return PageResponseDTO.from(boards.findByOwnerId(current.get().getId(),PageRequest.of(page,size,Sort.by(Sort.Direction.DESC,"createdAt","id"))).map(BoardResponseDTO::from));
    }
    @Transactional(readOnly=true) public BoardDetailResponseDTO detail(Long id,int page,int size){
        var board=owned(id,false);
        var ids=members.outfitIds(id,PageRequest.of(page,size));
        return new BoardDetailResponseDTO(BoardResponseDTO.from(board),
            new PageResponseDTO<>(reads.load(ids.getContent()),page,size,ids.getTotalElements(),ids.getTotalPages()));
    }
    @Transactional public void add(Long id,Long outfitId){
        var board=owned(id,true);
        var outfit=outfits.findById(outfitId).orElseThrow(()->new ResourceNotFoundException("Outfit not found"));
        if(members.lockMembership(id,outfitId).isEmpty())members.save(new BoardOutfit(board,outfit));
    }
    @Transactional public void remove(Long id,Long outfitId){
        owned(id,true);members.deleteByBoardIdAndOutfitId(id,outfitId);
    }
    @Transactional public void delete(Long id){
        var board=owned(id,true);members.deleteByBoardId(id);boards.delete(board);
    }
}
