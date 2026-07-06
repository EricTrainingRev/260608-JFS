document.getElementById('generate-btn').addEventListener('click', async () => {
    const resultEl = document.getElementById('result');

    try {
        const response = await fetch('/generate');
        const number = await response.text();
        resultEl.textContent = number;

        // trigger pop animation
        resultEl.classList.remove('pop');
        void resultEl.offsetWidth; // force reflow
        resultEl.classList.add('pop');
    } catch (error) {
        resultEl.textContent = 'Error!';
        console.error('Failed to fetch number:', error);
    }
});
