const profileModal = document.getElementById("profileModal");
const pagesModal = document.getElementById("pagesModal");
const settingsModal = document.getElementById("settingsModal");
const logoutModal = document.getElementById("logoutModal");

const openPagesModalBtn = document.getElementById("openPagesModal");
const openSettingsModalBtn = document.getElementById("openSettingsModal");
const openLogoutModalBtn = document.getElementById("openLogoutModal");


openPagesModalBtn.addEventListener("click", (event) => {
    event.preventDefault();
    pagesModal.style.display = "block";
});

openSettingsModalBtn.addEventListener("click", (event) => {
    event.preventDefault();
    settingsModal.style.display = "block";
});

openLogoutModalBtn.addEventListener("click", (event) => {
    event.preventDefault();
    logoutModal.style.display = "block";
});


const closeModals = () => {
    pagesModal.style.display = "none";
    settingsModal.style.display = "none";
    logoutModal.style.display = "none";
};

document.querySelectorAll(".closeModal").forEach(button => {
    button.addEventListener("click", closeModals);
});
window.addEventListener("click", (event) => {
    if (event.target === pagesModal) {
        pagesModal.style.display = "none";
    }
    if (event.target === settingsModal) {
        settingsModal.style.display = "none";
    }
    if (event.target === logoutModal) {
        logoutModal.style.display = "none";
    }
});


document.addEventListener('DOMContentLoaded', function () {
    console.log("DOM jest załadowany!");

    const tooltips = document.querySelectorAll('.reaction-tooltip');
    const statsTotal = document.querySelectorAll('.stats-total');
    const reactions = document.querySelectorAll('.reaction');
    const timelineFooters = document.querySelectorAll('.timeline-footer');
    function disableFooterHover() {
        timelineFooters.forEach(footer => {
            footer.classList.add('disable-hover');
        });
    }
    function enableFooterHover() {
        timelineFooters.forEach(footer => {
            footer.classList.remove('disable-hover');
        });
    }

    function showTooltip(el) {
        const tooltip = el.querySelector('.reaction-tooltip');
        if (tooltip) {
            hideTooltip();
            tooltip.style.display = 'block';
            tooltip.style.position = 'fixed';
            const rect = el.getBoundingClientRect();
            const tooltipHeight = tooltip.offsetHeight;
            const tooltipWidth = tooltip.offsetWidth;
            const padding = 5;
            const availableBottom = window.innerHeight - rect.bottom;
            const availableTop = rect.top;
            let topPosition = rect.bottom + padding;
            if (availableBottom < tooltipHeight + padding && availableTop >= tooltipHeight + padding) {
                topPosition = rect.top - tooltipHeight - padding;
            } else if (availableBottom < tooltipHeight + padding && availableTop < tooltipHeight + padding) {
                topPosition = availableBottom >= availableTop ? rect.bottom + padding : rect.top - tooltipHeight - padding;
                if (topPosition < padding) topPosition = padding;
                if (topPosition + tooltipHeight > window.innerHeight - padding) {
                    topPosition = window.innerHeight - tooltipHeight - padding;
                }
            }
            let leftPosition = rect.left;
            if (leftPosition + tooltipWidth > window.innerWidth - padding) {
                leftPosition = window.innerWidth - tooltipWidth - padding;
            }
            if (leftPosition < padding) leftPosition = padding;
            tooltip.style.top = topPosition + 'px';
            tooltip.style.left = leftPosition + 'px';

            disableFooterHover();
        }
    }

    function hideTooltip() {
        tooltips.forEach(t => t.style.display = 'none');
        enableFooterHover();
    }

    reactions.forEach(function (el) {
        el.addEventListener('mouseenter', function () {
            console.log("Tooltip dla reakcji pokazany");
            showTooltip(el);
        });
        el.addEventListener('mouseleave', function () {
        });
    });

    statsTotal.forEach(function (el) {
        el.addEventListener('mouseenter', function () {
            console.log("Tooltip dla stats-total pokazany");
            showTooltip(el);
        });
        el.addEventListener('mouseleave', function () {
        });
    });


    document.addEventListener('click', function (e) {
        const isInsideTooltip = e.target.closest('.reaction-tooltip');
        const isInsideReaction = e.target.closest('.reaction');
        const isInsideStats = e.target.closest('.stats-total');
        if (!isInsideTooltip && !isInsideReaction && !isInsideStats) {
            console.log("Kliknięto poza tooltipem, ukrywam");
            hideTooltip();
        }
    });

    window.addEventListener('scroll', function () {
        console.log("Scroll strony – ukrywam tooltip");
        hideTooltip();
    });
});

document.addEventListener('DOMContentLoaded', function () {
    const currentLoggedUserId = window.currentLoggedUserId || null;

    document.querySelectorAll('.reaction-option').forEach(function (emoji) {
        emoji.addEventListener('click', function () {
            const reactionType = this.dataset.reaction;
            const wrapper = this.closest('.reaction-button-wrapper');
            const postId = wrapper.dataset.postId;

            fetch(`/api/reactions`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    userPostId: postId,
                    reactionType: reactionType
                })
            })
                .then(response => {
                    if (response.ok) {
                        const label = wrapper.querySelector('.reaction-label');
                        const currentReaction = label.textContent.trim();

                        if (currentReaction === this.textContent.trim()) {
                            label.innerHTML = '<i class="fa fa-thumbs-up fa-fw fa-lg m-r-3"></i> Lubię to';
                        } else {
                            label.textContent = this.textContent;
                        }
                        window.location.reload();
                    } else {
                        alert("Błąd podczas dodawania reakcji");
                    }
                });
        });
    });
});
document.querySelectorAll('textarea.auto-expand').forEach(textarea => {
    textarea.addEventListener('input', () => {
        textarea.style.height = 'auto';
        textarea.style.height = textarea.scrollHeight + 'px';
    });
});