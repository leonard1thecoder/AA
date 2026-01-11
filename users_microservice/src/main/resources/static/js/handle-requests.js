
document.addEventListener("DOMContentLoaded", () => {
  const form = document.getElementById("contactForm");
  const popup = document.getElementById("popup");
  const popupTitle = document.getElementById("popupTitle");
  const popupMessage = document.getElementById("popupMessage");
  const okBtn = document.getElementById("okBtn"); // reference the OK button

  form.addEventListener("submit", async (event) => {
    event.preventDefault();

    const formData = {
      name: document.getElementById("name").value,
      surname: document.getElementById("surname").value,
      email: document.getElementById("email").value,
      message: document.getElementById("message").value
    };

    try {
      const response = await fetch("/api/contact/submit", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(formData)
      });

      console.log(JSON.stringify(formData));
      const result = await response.json();
      console.log(result);

      if (result.status === "success") {
        popupTitle.textContent = "Success";
        popupTitle.classList.remove("error-message");
        popupMessage.textContent = result.message;
        popup.style.display = "flex";
        form.reset();
      } else {
        popupTitle.textContent = "Error";
        popupTitle.classList.add("error-message");
        popupMessage.textContent = result.message;
        popup.style.display = "flex";
      }
    } catch (error) {
      popupTitle.textContent = "Error2";
      popupTitle.classList.add("error-message");
      popupMessage.textContent = "Server not reachable";
      popup.style.display = "flex";
    }
  });

  // ✅ Close popup when OK is clicked
  okBtn.addEventListener("click", () => {
    popup.style.display = "none";
  });
});