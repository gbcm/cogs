package io.pivotal.controller;

import feign.Feign;
import feign.gson.GsonDecoder;
import io.pivotal.config.Config;
import io.pivotal.service.IFeignDiscogsService;
import io.pivotal.service.response.ArtistSearchResponse;
import io.pivotal.view.form.SubmittedArtistId;
import io.pivotal.model.Artist;
import io.pivotal.model.ArtistRepository;
import io.pivotal.view.form.SubmittedArtistName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
public class ArtistTrackController {

    @Autowired
    ArtistRepository artistRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home(Model model, SubmittedArtistId submittedArtistId, SubmittedArtistName submittedArtistName){
        model.addAttribute("artists", artistRepository.findAll());
        return "index";
    }

    @RequestMapping(value = "/searchResults", method = RequestMethod.POST)
    public String searchForArtist(@Valid SubmittedArtistName submittedArtistName, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "index";
        }

        IFeignDiscogsService discogsService = Feign.builder()
                .decoder(new GsonDecoder())
                .target(IFeignDiscogsService.class, "https://api.discogs.com/");
        ArtistSearchResponse asr = discogsService.searchArtists(submittedArtistName.getArtistName(), Config.getDiscogsToken());
        model.addAttribute("artists", asr.getResults());
        return "searchResults";
    }
}
