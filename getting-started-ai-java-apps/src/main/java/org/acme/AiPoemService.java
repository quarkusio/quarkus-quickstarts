package org.acme;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import io.quarkiverse.langchain4j.RegisterAiService;

@RegisterAiService( ) // <1>
public interface AiPoemService {

    @SystemMessage("You are a professional poet. Display the poem in well-formed HTML with line breaks (no markdown).") // <2>
    @UserMessage("Write a poem about {poemTopic}. The poem should be {poemLines} lines long.") // <3>
    String writeAPoem(String poemTopic, int poemLines); // <4>
}
