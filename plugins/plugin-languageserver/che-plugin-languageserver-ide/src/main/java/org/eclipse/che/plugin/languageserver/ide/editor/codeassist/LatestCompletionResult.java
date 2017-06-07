/*******************************************************************************
 * Copyright (c) 2016 Rogue Wave Software, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Rogue Wave Software, Inc. - initial API and implementation
 *******************************************************************************/
package org.eclipse.che.plugin.languageserver.ide.editor.codeassist;

import org.eclipse.che.api.languageserver.shared.model.ExtendedCompletionItem;
import org.eclipse.lsp4j.TextDocumentIdentifier;

import java.util.List;

/**
 * Contains the latest completion result retrieved from the completion service.
 *
 * @author Kaloyan Raev
 */
public class LatestCompletionResult {

    private TextDocumentIdentifier documentId;
    private int                    offset;
    private String                 word;
    private List<ExtendedCompletionItem> completionList;

    /**
     * Returns the identifier of document used to compute the latest completion
     * result.
     *
     * @return the document identifier
     */
    public TextDocumentIdentifier getDocumentId() {
        return this.documentId;
    }

    /**
     * Returns the offset position in document used to compute the latest
     * completion result.
     *
     * @return the offset
     */
    public int getOffset() {
        return this.offset;
    }

    /**
     * Returns the word at the cursor at the time of computing the latest
     * completion result.
     *
     * @return the word
     */
    public String getWord() {
        return this.word;
    }

    /**
     * Returns the latest completion list DTO object.
     *
     * @return the completion list
     */
    public List<ExtendedCompletionItem> getCompletionList() {
        return this.completionList;
    }

    /**
     * Checks if the completion result is still good for the given document
     * position.
     * <p>
     * <p>
     * The following checks are executed:
     * <ol>
     * <li>A completion result has been retrieved at least once.</li>
     * <li>The latest completion result is "complete", i.e. the
     * <code>isIncomplete</code> property is <code>false</code>.
     * <li>The given document id is the same as in the latest completion
     * result.</li>
     * <li>The given word starts with the one in the latest completion
     * result.</li>
     * <li>The difference between the given offset and the one in the latest
     * completion result matches the respective difference between the
     * words.</li>
     * </ol>
     * Only if all checks are satisfied then the latest completion result can be
     * reused for the given document position.
     * </p>
     *
     * @param documentId
     *         a text document identifier
     * @param offset
     *         an offset position in the document
     * @param word
     *         the word at the current position in the document
     * @return <code>true</code> if the completion result can still be used for
     * the given document position, <code>false</code> otherwise.
     */
    public boolean isGoodFor(TextDocumentIdentifier documentId, int offset, String word) {
        return completionList != null &&
//               !completionList.isInComplete() && //todo rework it
               this.documentId.getUri().equals(documentId.getUri()) &&
               word.startsWith(this.word) &&
               offset - this.offset == word.length() - this.word.length();
    }

    /**
     * Updates the latest completion result.
     *
     * @param documentId
     *         a text document identifier
     * @param offset
     *         an offset position in the document
     * @param word
     *         the word at the current position in the document
     * @param completionList
     *         a completion list
     */
    public void update(TextDocumentIdentifier documentId, int offset, String word,
                       List<ExtendedCompletionItem> completionList) {
        this.documentId = documentId;
        this.offset = offset;
        this.word = word;
        this.completionList = completionList;
    }

}
