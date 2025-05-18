document.addEventListener('DOMContentLoaded', function () {
    console.log("DOM załadowany!");

    const profileModal = document.getElementById("profileModal");
    const pagesModal = document.getElementById("pagesModal");
    const settingsModal = document.getElementById("settingsModal");
    const logoutModal = document.getElementById("logoutModal");

    const openPagesModalBtn = document.getElementById("openPagesModal");
    const openSettingsModalBtn = document.getElementById("openSettingsModal");
    const openLogoutModalBtn = document.getElementById("openLogoutModal");

    if (openPagesModalBtn) {
        openPagesModalBtn.addEventListener("click", (event) => {
            event.preventDefault();
            pagesModal.style.display = "block";
        });
    }

    if (openSettingsModalBtn) {
        openSettingsModalBtn.addEventListener("click", (event) => {
            event.preventDefault();
            settingsModal.style.display = "block";
        });
    }

    if (openLogoutModalBtn) {
        openLogoutModalBtn.addEventListener("click", (event) => {
            event.preventDefault();
            logoutModal.style.display = "block";
        });
    }

    const closeModals = () => {
        if (pagesModal) pagesModal.style.display = "none";
        if (settingsModal) settingsModal.style.display = "none";
        if (logoutModal) logoutModal.style.display = "none";
    };

    document.querySelectorAll(".closeModal").forEach(button => {
        button.addEventListener("click", closeModals);
    });

    window.addEventListener("click", (event) => {
        if (event.target === pagesModal) pagesModal.style.display = "none";
        if (event.target === settingsModal) settingsModal.style.display = "none";
        if (event.target === logoutModal) logoutModal.style.display = "none";
    });


    function bindReactionTooltips() {
        const tooltips = document.querySelectorAll('.reaction-tooltip');
        const reactions = document.querySelectorAll('.reaction');
        const timelineFooters = document.querySelectorAll('.timeline-footer');

        function disableFooterHover() {
            timelineFooters.forEach(footer => footer.classList.add('disable-hover'));
        }

        function enableFooterHover() {
            timelineFooters.forEach(footer => footer.classList.remove('disable-hover'));
        }

        function showTooltip(el) {
            const tooltip = el.querySelector('.reaction-tooltip');
            if (!tooltip) return;

            hideTooltip();

            tooltip.style.display = 'block';
            tooltip.style.position = 'absolute';

            tooltip.style.top = '';
            tooltip.style.left = '';

            tooltip.style.top = 'calc(100% + 8px)';
            tooltip.style.left = '0';

            disableFooterHover();
        }


        function hideTooltip() {
            tooltips.forEach(t => t.style.display = 'none');
            enableFooterHover();
        }

        reactions.forEach(el => {
            const triggerTarget = el.querySelector('.emoji') || el;

            triggerTarget.addEventListener('mouseenter', () => showTooltip(el));
            triggerTarget.addEventListener('mouseleave', () => {
                const tooltip = el.querySelector('.reaction-tooltip');
                setTimeout(() => {
                    if (!el.matches(':hover') && !tooltip.matches(':hover')) {
                        tooltip.style.display = 'none';
                        enableFooterHover();
                    }
                }, 200);
            });
        });

        document.querySelectorAll('.comment-count').forEach(el => {
            const reactionEl = el.closest('.reaction');
            const tooltip = reactionEl?.querySelector('.reaction-tooltip');
            if (!tooltip) return;

            el.addEventListener('mouseenter', () => {
                hideTooltip();
                tooltip.style.display = 'block';
                tooltip.style.position = 'absolute';
                tooltip.style.top = 'calc(100% + 8px)';
                tooltip.style.left = '0';
            });

            el.addEventListener('mouseleave', () => {
                setTimeout(() => {
                    if (!el.matches(':hover') && !tooltip.matches(':hover')) {
                        tooltip.style.display = 'none';
                    }
                }, 200);
            });
        });



        document.addEventListener('click', function (e) {
            const isInsideTooltip = e.target.closest('.reaction-tooltip');
            const isInsideReaction = e.target.closest('.reaction');
            if (!isInsideTooltip && !isInsideReaction) hideTooltip();
        });

        window.addEventListener('scroll', hideTooltip);
        console.log('Związano tooltipy z', reactions.length, 'reakcjami');
    }

    bindReactionTooltips();


    const currentLoggedUserId = window.currentLoggedUserId || null;

    document.querySelectorAll('.reaction-button-wrapper[data-post-id] .reaction-option').forEach(function (emoji) {
        emoji.addEventListener('click', function () {
            const reactionType = this.dataset.reaction;
            const wrapper = this.closest('.reaction-button-wrapper');
            const postId = wrapper.dataset.postId;
            const label = wrapper.querySelector('.reaction-label');
            const currentReaction = label.textContent.trim();

            fetch(`/api/reactions`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'same-origin',
                body: JSON.stringify({
                    userPostId: postId,
                    reactionType: reactionType
                })
            })
                .then(response => {
                    if (response.ok || response.status === 204) {
                        if (currentReaction === emoji.textContent.trim()) {
                            label.innerHTML = '<i class="fa fa-thumbs-up fa-fw fa-lg m-r-3"></i> Lubię to';
                        } else {
                            label.textContent = emoji.textContent;
                        }
                        window.location.reload();
                    } else {
                        alert("Błąd podczas dodawania reakcji do posta");
                    }
                })
                .catch(error => {
                    console.error("Błąd sieci:", error);
                    alert("Nie udało się dodać reakcji do posta.");
                });
        });
    });


    document.querySelectorAll('textarea.auto-expand').forEach(textarea => {
        textarea.addEventListener('input', () => {
            textarea.style.height = 'auto';
            textarea.style.height = textarea.scrollHeight + 'px';
        });
    });


    const postForm = document.getElementById('postForm');
    if (postForm) {
        postForm.addEventListener('submit', async function (e) {
            e.preventDefault();

            clearErrors();

            const content = document.getElementById('postContent').value.trim();

            const postDto = {
                content: content,
                postType: "USER_POST"
            };

            try {
                const response = await fetch('/api/posts', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(postDto)
                });

                if (response.ok) {
                    const newPost = await response.json();
                    console.log('Post dodany:', newPost);
                    location.reload();
                } else if (response.status === 400) {
                    const errorData = await response.json();
                    for (const message of Object.values(errorData)) {
                        showError(`${message}`);
                    }
                } else {
                    alert("Wystąpił błąd przy dodawaniu posta.");
                }
            } catch (error) {
                console.error('Błąd:', error);
                alert("Błąd sieci lub serwera.");
            }
        });
    }

    function showError(message) {
        const errorDiv = document.getElementById('errorDiv');
        errorDiv.innerHTML += `<p style="color:red;">${message}</p>`;
    }

    function clearErrors() {
        const errorDiv = document.getElementById('errorDiv');
        errorDiv.innerHTML = '';
    }


    document.querySelectorAll('.commentForm').forEach(form => {
        form.addEventListener('submit', async e => {
            e.preventDefault();

            const commentInput = form.querySelector('.commentContent');
            const commentContent = commentInput.value.trim();
            if (!commentContent) {
                const errorDiv = form.querySelector(`[id^='errorDiv-post-']`);
                if (errorDiv) {
                    errorDiv.innerHTML = `<p style="color:red;">Pole nie może być puste</p>`;
                }
                return;
            }

            const quotedCommentId = commentInput.getAttribute('data-quoted-comment-id') || null;

            const timelineBox = form.closest('.timeline-comment-box');
            const postId = timelineBox.dataset.postId;

            try {
                const response = await fetch('/api/user-post-comments', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify({
                        content: commentContent,
                        userPostId: postId,
                        quotedCommentId: quotedCommentId
                    })
                });

                if (!response.ok) {
                    console.error('Błąd podczas dodawania komentarza.');
                    return;
                }

                console.log('Komentarz dodany – odświeżam');
                commentInput.value = '';
                commentInput.removeAttribute('data-quoted-comment-id');
                localStorage.setItem('openCommentsPostId', postId);
                location.reload();

            } catch (error) {
                console.error('Błąd sieci przy dodawaniu komentarza:', error);
            }
        });
    });



    const commentButtons = document.querySelectorAll(".comment-trigger");
    commentButtons.forEach(button => {
        button.addEventListener("click", () => {
            const postId = button.getAttribute("data-post-id");
            const commentList = document.getElementById(`comments-${postId}`);
            if (commentList) {
                const visible = commentList.classList.contains("visible");
                commentList.classList.toggle("visible");
                commentList.style.display = visible ? 'none' : 'block';
            }
        });
    });

    const postId = localStorage.getItem('openCommentsPostId');
    const commentId = localStorage.getItem('highlightCommentId');

    if (postId) {
        const commentList = document.getElementById(`comments-${postId}`);
        if (commentList) {
            commentList.classList.add("visible");
            commentList.style.display = 'block';
        }

        if (commentId) {
            const commentElement = document.querySelector(`.comment-item[data-comment-id="${commentId}"]`);
            if (commentElement) {
                commentElement.scrollIntoView({ behavior: "smooth", block: "center" });
                commentElement.classList.add("highlight-comment");
            }
            localStorage.removeItem('highlightCommentId');
        }

        localStorage.removeItem('openCommentsPostId');
    }


    document.querySelectorAll('.reaction-button-wrapper[data-comment-id] .reaction-option').forEach(function (emoji) {
        emoji.addEventListener('click', function () {
            const reactionType = this.dataset.reaction;
            const wrapper = this.closest('.reaction-button-wrapper');
            const commentId = wrapper.dataset.commentId;
            const postId = wrapper.dataset.postId;
            const label = wrapper.querySelector('.reaction-label');
            const currentReaction = label.textContent.trim();

            fetch(`/api/post-comments/reactions`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                credentials: 'same-origin',
                body: JSON.stringify({
                    postCommentId: commentId,
                    reactionType: reactionType
                })
            })
                .then(response => {
                    if (response.ok || response.status === 204) {
                        if (currentReaction === emoji.textContent.trim()) {
                            label.innerHTML = '<i class="fa fa-thumbs-up fa-fw fa-lg m-r-3"></i> Lubię to';
                        } else {
                            label.textContent = emoji.textContent;
                        }
                        localStorage.setItem('openCommentsPostId', postId);
                        localStorage.setItem('highlightCommentId', commentId);
                        window.location.reload();
                    } else {
                        alert("Błąd podczas dodawania reakcji do komentarza");
                    }
                })
                .catch(error => {
                    console.error("Błąd sieci:", error);
                    alert("Nie udało się dodać reakcji do komentarza.");
                });
        });
    });

    document.querySelectorAll('.reply-btn').forEach(button => {
        button.addEventListener('click', () => {
            const commentId = button.getAttribute('data-comment-id');
            const commentElement = document.querySelector(`.comment-item[data-comment-id="${commentId}"]`);
            const content = commentElement.querySelector('.timeline-content p').textContent;
            const author = commentElement.querySelector('.timeline-header a').textContent;

            const timelineBox = button.closest('.timeline-body');
            const commentForm = timelineBox.querySelector('.commentForm');
            const textarea = commentForm.querySelector('.commentContent');

            textarea.value = '';
            textarea.setAttribute('data-quoted-comment-id', commentId);
            textarea.focus();
        });
    });

    document.addEventListener('click', function (e) {
        const quoteLink = e.target.closest('.quote-link');
        if (quoteLink) {
            const quotedId = quoteLink.getAttribute('data-comment-id');
            const quotedEl = document.querySelector(`.comment-item[data-comment-id="${quotedId}"]`);
            if (quotedEl) {
                quotedEl.scrollIntoView({ behavior: 'smooth', block: 'center' });
                quotedEl.classList.add('highlight-comment');
                setTimeout(() => quotedEl.classList.remove('highlight-comment'), 2000);
            }
        }
    });


    document.querySelectorAll('#sortComments').forEach(select => {
        select.addEventListener('change', async function () {
            const sortOrder = this.value;
            const postId = this.closest('.comment-list').id.replace('comments-', '');

            try {
                const response = await fetch(`/api/user-post-comments/${postId}?sortOrder=${sortOrder}`);
                if (response.ok) {
                    const comments = await response.json();
                    updateCommentsOrder(postId, comments);
                } else {
                    alert("Błąd podczas ładowania komentarzy.");
                }
            } catch (error) {
                console.error("Błąd sieci:", error);
                alert("Nie udało się załadować komentarzy.");
            }
        });
    });

    function updateCommentsOrder(postId, comments) {
        const commentList = document.getElementById(`comments-${postId}`);
        const commentItemsMap = new Map();

        commentList.querySelectorAll('.comment-item').forEach(item => {
            const commentId = item.getAttribute('data-comment-id');
            commentItemsMap.set(commentId, item);
        });

        const sorterElement = commentList.querySelector('.comment-sorter');

        commentList.innerHTML = '';
        if (sorterElement) commentList.appendChild(sorterElement);

        comments.forEach(comment => {
            const commentElement = commentItemsMap.get(String(comment.id));
            if (commentElement) {
                commentList.appendChild(commentElement);
            }
        });
    }

    document.addEventListener('click', function (e) {
        if (e.target.classList.contains('comment-options-btn')) {
            e.stopPropagation();

            const menu = e.target.nextElementSibling;
            const isVisible = menu.style.display === 'block';

            document.querySelectorAll('.comment-options-menu').forEach(m => m.style.display = 'none');
            menu.style.display = isVisible ? 'none' : 'block';
        } else {
            document.querySelectorAll('.comment-options-menu').forEach(m => m.style.display = 'none');
        }
    });

    const deleteModal = document.getElementById('deleteCommentModal');
    const reportModal = document.getElementById('reportCommentModal');
    const confirmDeleteBtn = document.getElementById('confirmDeleteCommentBtn');
    const confirmReportBtn = document.getElementById('confirmReportCommentBtn');

    let currentCommentId = null;

    function openModal(modalType, commentId) {
        currentCommentId = commentId;
        if (modalType === 'delete') {
            deleteModal.style.display = 'block';
        } else if (modalType === 'report') {
            reportModal.style.display = 'block';
        }
    }

    document.addEventListener('click', (e) => {
        if (e.target.classList.contains('delete-comment-btn')) {
            const commentId = e.target.getAttribute('data-comment-id');
            openModal('delete', commentId);
        }
        if (e.target.classList.contains('report-comment-btn')) {
            const commentId = e.target.closest('.comment-item')?.getAttribute('data-comment-id');
            openModal('report', commentId);
        }
    });

    confirmDeleteBtn.addEventListener('click', () => {
        if (!currentCommentId) return;

        const commentElement = document.querySelector(`.comment-item[data-comment-id="${currentCommentId}"]`);
        const postId = commentElement?.closest('.timeline-body')?.parentElement?.dataset.postId;

        fetch(`/api/user-post-comments/${currentCommentId}`, { method: 'DELETE' })
            .then(response => {
                if (response.ok) {
                    if (postId) {
                        localStorage.setItem('openCommentsPostId', postId);
                    }
                    location.reload();
                } else {
                    alert('Błąd podczas usuwania komentarza.');
                }
            })
            .finally(() => {
                closeAllModals();
            });
    });

    confirmReportBtn.addEventListener('click', () => {
        if (!currentCommentId) return;

        fetch(`/api/user-post-comments/report/${currentCommentId}`, { method: 'POST' })
            .then(response => {
                if (response.ok) {
                    alert('Komentarz został zgłoszony.');
                } else {
                    alert('Błąd podczas zgłaszania komentarza.');
                }
            }).finally(() => {
            closeAllModals();
        });
    });


    document.querySelectorAll('.closeModal').forEach(btn => {
        btn.addEventListener('click', closeAllModals);
    });

    window.addEventListener('click', (e) => {
        if (e.target === deleteModal || e.target === reportModal) {
            closeAllModals();
        }
    });

    function closeAllModals() {
        deleteModal.style.display = 'none';
        reportModal.style.display = 'none';
        currentCommentId = null;
    }

    document.addEventListener('click', function (e) {
        if (e.target.classList.contains('edit-comment-btn')) {
            const commentItem = e.target.closest('.comment-item');
            const commentText = commentItem.querySelector('.timeline-content p');
            const existingEditArea = commentItem.querySelector('.edit-comment-area');

            if (!existingEditArea) {
                const editArea = document.createElement('div');
                editArea.classList.add('edit-comment-area');

                editArea.innerHTML = `
                <textarea class="edit-comment-textarea form-control" rows="3"></textarea>
                <div style="margin-top: 10px;">
                    <button class="btn btn-success save-edit-btn">Zapisz</button>
                    <button class="btn btn-secondary cancel-edit-btn">Anuluj</button>
                </div>
            `;

                commentItem.querySelector('.timeline-content').appendChild(editArea);
            }


            const textarea = commentItem.querySelector('.edit-comment-textarea');
            textarea.value = commentText.textContent.trim();

            commentItem.querySelector('.edit-comment-area').style.display = 'block';
            commentText.style.display = 'none';
        }

        if (e.target.classList.contains('cancel-edit-btn')) {
            const commentItem = e.target.closest('.comment-item');
            commentItem.querySelector('.edit-comment-area').style.display = 'none';
            commentItem.querySelector('.timeline-content p').style.display = 'block';
        }

        if (e.target.classList.contains('save-edit-btn')) {
            const commentItem = e.target.closest('.comment-item');
            const commentId = commentItem.getAttribute('data-comment-id');
            const textarea = commentItem.querySelector('.edit-comment-textarea');
            const newText = textarea.value.trim();
            const commentText = commentItem.querySelector('.timeline-content p');
            const errorDiv = commentItem.querySelector(`#editErrorDiv-${commentId}`);

            if (errorDiv) errorDiv.innerHTML = '';

            fetch(`/api/user-post-comments/${commentId}`, {
                method: 'PUT',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ content: newText })
            })
                .then(async response => {
                    if (response.ok) {
                        commentText.textContent = newText;
                        commentItem.querySelector('.edit-comment-area').style.display = 'none';
                        commentText.style.display = 'block';
                    } else if (response.status === 400) {
                        const errorData = await response.json();
                        if (errorDiv) {
                            Object.values(errorData).forEach(message => {
                                errorDiv.innerHTML += `<p style="color:red; margin:0;">${message}</p>`;
                            });
                        }
                    } else {
                        alert('Błąd podczas edycji komentarza.');
                    }
                })
                .catch(error => {
                    console.error('Błąd sieci przy edycji:', error);
                    alert('Nie udało się edytować komentarza.');
                });
        }



    });


});