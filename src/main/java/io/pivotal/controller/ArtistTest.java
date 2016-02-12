package io.pivotal.controller;

import feign.Feign;
import io.pivotal.IFeignDiscogsService;
import io.pivotal.SubmittedArtistId;
import io.pivotal.model.Artist;
import io.pivotal.model.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
public class ArtistTest {

    @Autowired
    ArtistRepository artistRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String doTheThing(Model model, SubmittedArtistId submittedArtistId){
        model.addAttribute("artists", artistRepository.findAll());
        return "index";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String addNewPost(@Valid SubmittedArtistId submittedArtistId, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "index";
        }

        IFeignDiscogsService discogsService = Feign.builder()
                .target(IFeignDiscogsService.class, "https://api.discogs.com/");

        int i = discogsService.getReleases(submittedArtistId.getDiscogId()).getReleases();
        Artist a = new Artist(submittedArtistId.getDiscogId(), submittedArtistId.getDiscogId(), i);
        artistRepository.save(a);
        return doTheThing(model, submittedArtistId);
    }
}
