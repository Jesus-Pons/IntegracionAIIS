package aiss.videominer.controllers;

import aiss.videominer.exception.CaptionNotFoundException;
import aiss.videominer.model.Caption;
import aiss.videominer.model.Channel;
import aiss.videominer.repository.CaptionRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
@Tag(name="Caption",description = "Caption from a video")
@RestController
@RequestMapping("/videominer")
public class CaptionController {
    @Autowired
    CaptionRepository captionRepository;

    @Operation(
            summary = "Retrieve a List of captions",
            description = "Get all captions",
            tags = {"caption","get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Caption.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})})
    @GetMapping("/captions")
    public List<Caption> getAllCaptions(@Parameter(description = "filter by language")@RequestParam(required = false) String language,
                                        @Parameter(description = "order of the requested captions (- before attsribute for desc)")@RequestParam(required = false) String order,
                                        @Parameter(description = "number of the page you want to see")@RequestParam(defaultValue = "0") int page,
                                        @Parameter(description = "number of channels per page")@RequestParam(defaultValue = "10") int size){

        Pageable paging;
        if(order != null){
            if(order.startsWith("-")){
                paging = PageRequest.of(page,size,Sort.by(order.substring(1)).descending());
            }else{
                paging = PageRequest.of(page,size,Sort.by(order).ascending());
            }

        }else{
            paging = PageRequest.of(page,size);
        }
        Page<Caption> pageProjects;
        if(language==null){
            pageProjects = captionRepository.findAll(paging);
        }else{
            pageProjects = captionRepository.findByLanguage(language,paging);
        }
        return pageProjects.getContent();
    }
    @Operation(
            summary = "Retrieve a captions",
            description = "Get caption by id",
            tags = {"caption","get"}
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200",content = {@Content(schema = @Schema(implementation = Caption.class),mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", content = {@Content(schema = @Schema())})})
    @GetMapping("/captions/{captionId}")
    public Caption getCaptionById(@Parameter(description = "id of the caption to be search") @PathVariable(value = "captionId") String captionId) throws CaptionNotFoundException {
        Optional<Caption> caption= captionRepository.findById(captionId);
        if(!caption.isPresent()){
            throw new CaptionNotFoundException();
        }
        return caption.get();
    }
}
