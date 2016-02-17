package io.pivotal.controller;

import feign.Feign;
import feign.gson.GsonDecoder;
import io.pivotal.config.Config;
import io.pivotal.model.*;
import io.pivotal.service.IFeignDiscogsService;
import io.pivotal.service.response.ArtistSearchResponse;
import io.pivotal.service.response.ReleasesResponse;
import io.pivotal.view.form.ArtistReleasePresenter;
import io.pivotal.view.form.SubmittedArtistName;
import io.pivotal.view.form.SubmittedCheckedReleases;
import io.pivotal.view.form.SubmittedUserName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("userId")
public class ArtistTrackController {

    @Autowired
    ArtistRepository artistRepository;
    @Autowired
    CogsUserRepository cogsUserRepository;
    @Autowired
    ReleaseRepository releaseRepository;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String indexGet(SubmittedUserName submittedUserName) {
        return "index";
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public String indexPost(SubmittedUserName submittedUserName, Model model) {
        CogsUser cogsUser = cogsUserRepository.findByNameIgnoreCase(submittedUserName.getUserName());
        if (cogsUser == null) {
            cogsUser = new CogsUser(submittedUserName.getUserName());
            cogsUser = cogsUserRepository.save(cogsUser);
        }
        model.addAttribute("userId", cogsUser.getId());
        return "redirect:/home/";
    }

    @RequestMapping(value = "/home/", method = RequestMethod.GET)
    public String homeGet(@ModelAttribute("userId") long userId,
                          Model model) {
        CogsUser cogsUser = cogsUserRepository.findOne(userId);
        model.addAttribute("userName", cogsUser.getName());

        List<ArtistReleasePresenter> arps = cogsUser.getTrackedArtists().stream()
                .map(x -> new ArtistReleasePresenter(x.getName(),
                        getReleases(x.getDiscogsId()).stream()
                                .filter(z -> !cogsUser.getHiddenReleases().contains(z))
                                .map(y -> y.getName()).collect(Collectors.toList())))
                .collect(Collectors.toList());

        model.addAttribute("submittedArtistName", new SubmittedArtistName());
        model.addAttribute("artists", arps);
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
    public String artistReleasesGet(Model model,
                                    @PathVariable String discogsId,
                                    SubmittedCheckedReleases submittedCheckedReleases) {
        IFeignDiscogsService discogsService = Feign.builder()
                .decoder(new GsonDecoder())
                .target(IFeignDiscogsService.class, "https://api.discogs.com/");

        ReleasesResponse rel = discogsService.getReleases(discogsId);
        List<ReleasesResponse.Release> releases = rel.getRelevantReleases();
        model.addAttribute("releases", releases);
        model.addAttribute("artistDiscogsId", discogsId);
        model.addAttribute("artistName", releases.get(0).getArtist());
        return "artistReleases";
    }

    @RequestMapping(value = "/artistReleases/", method = RequestMethod.POST)
    public String artistReleasesPost(SubmittedCheckedReleases submittedCheckedReleases,
                                     @ModelAttribute("artistDiscogsId") String artistDiscogsId,
                                     @ModelAttribute("userId") long userId,
                                     @ModelAttribute("artistName") String artistName,
                                     Model model) {

        CogsUser cogsUser = cogsUserRepository.findOne(userId);
        Artist artist = artistRepository.findByDiscogsIdIgnoreCase(artistDiscogsId);
        if (artist == null) {
            artist = new Artist(artistName, artistDiscogsId);
            artist = artistRepository.save(artist);
        }

        for (Release release : getReleases(artistDiscogsId)) {
            Release temp = releaseRepository.findByDiscogsIdIgnoreCase(release.getDiscogsId());
            if (temp == null) {
                temp = releaseRepository.save(release);
            }
            artist.getReleases().add(temp);
            if (submittedCheckedReleases.getReleaseDiscogsIds().contains(release.getDiscogsId())) {
                cogsUser.getHiddenReleases().add(release);
            }
        }
        artistRepository.save(artist);

        cogsUser.getTrackedArtists().add(artist);
        cogsUserRepository.save(cogsUser);

        model.addAttribute("userName", cogsUser.getName());
        model.addAttribute("artists", cogsUser.getTrackedArtists());

        return homeGet(userId ,model);
    }

    private List<Release> getReleases(@ModelAttribute("artistDiscogsId") String artistDiscogsId) {
        IFeignDiscogsService discogsService = Feign.builder()
                .decoder(new GsonDecoder())
                .target(IFeignDiscogsService.class, "https://api.discogs.com/");

        ReleasesResponse rel = discogsService.getReleases(artistDiscogsId);
        return rel.getRelevantReleases().stream()
                .map(r -> new Release(
                        r.getTitle(),
                        String.format("%d", r.getDiscogsId()),
                        r.getYear()))
                .collect(Collectors.toList());
    }
}
