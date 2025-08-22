package sa3.fijiimpulse.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PathVariableController {
    @GetMapping("/posts/{id}/comments/{commentId}")
    public String getPostId(
            @PathVariable("id") String postId,
            @PathVariable("commentId") String inputCommentId
    ) {
        return postId + " " + inputCommentId;
    }
}
