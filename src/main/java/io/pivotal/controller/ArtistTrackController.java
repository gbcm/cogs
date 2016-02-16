package io.pivotal.controller;

import feign.Feign;
import feign.gson.GsonDecoder;
import io.pivotal.config.Config;
import io.pivotal.model.CogsUser;
import io.pivotal.model.CogsUserRepository;
import io.pivotal.service.IFeignDiscogsService;
import io.pivotal.service.response.ArtistSearchResponse;
import io.pivotal.service.response.ReleasesResponse;
import io.pivotal.model.ArtistRepository;
import io.pivotal.view.form.SubmittedArtistName;
import io.pivotal.view.form.SubmittedUserName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
public class ArtistTrackController {

    @Autowired
    ArtistRepository artistRepository;
    @Autowired
    CogsUserRepository cogsUserRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String indexGet(SubmittedUserName submittedUserName){
        return "index";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String indexPost(SubmittedUserName submittedUserName){
        CogsUser cogsUser = cogsUserRepository.findByNameIgnoreCase(submittedUserName.getUserName());
        if (cogsUser == null) {
            cogsUser = new CogsUser(submittedUserName.getUserName());
            cogsUser = cogsUserRepository.save(cogsUser);
        }
        return "redirect:/home/" + cogsUser.getId();
    }

    @RequestMapping(value = "/home/{userId}", method = RequestMethod.GET)
    public String homeGet(@PathVariable long userId, Model model, SubmittedArtistName submittedArtistName){
        CogsUser cogsUser = cogsUserRepository.findOne(userId);
        model.addAttribute("userName", cogsUser.getName());
        model.addAttribute("artists", cogsUser.getTrackedArtists());
        return "home";
    }

    @RequestMapping(value = "/home/", method = RequestMethod.POST)
    public String homePost(@Valid SubmittedArtistName submittedArtistName, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "index";
        }
        return "redirect:/searchResults/" + submittedArtistName.getArtistName();
    }

    @RequestMapping(value = "/searchResults/{artistName}", method = RequestMethod.GET)
    public String searchResultsGet(@PathVariable String artistName, Model model) {
        IFeignDiscogsService discogsService = Feign.builder()
                .decoder(new GsonDecoder())
                .target(IFeignDiscogsService.class, "https://api.discogs.com/");
        ArtistSearchResponse asr = discogsService.searchArtists(artistName, Config.getDiscogsToken());
        model.addAttribute("artists", asr.getResults());
        return "searchResults";
    }

    @RequestMapping(value = "/artistReleases/{discogsId}", method = RequestMethod.GET)
    public String artistReleasesGet(Model model, @PathVariable String discogsId) {
        IFeignDiscogsService discogsService = Feign.builder()
                .decoder(new GsonDecoder())
                .target(IFeignDiscogsService.class, "https://api.discogs.com/");

        ReleasesResponse rel = discogsService.getReleases(discogsId);
        model.addAttribute("releases", rel.getRelevantReleases());
        return "artistReleases";
    }
}
