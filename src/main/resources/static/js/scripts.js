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

