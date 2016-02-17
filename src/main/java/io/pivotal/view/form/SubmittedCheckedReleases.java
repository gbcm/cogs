package io.pivotal.view.form;

import lombok.Data;

import javax.validation.constraints.Size;
import java.util.List;

@Data
public class SubmittedCheckedReleases {
    private List<String> releaseDiscogsIds;
}
