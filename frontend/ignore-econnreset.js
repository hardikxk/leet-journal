process.on('uncaughtException', (err) => {
  if (err.code === 'ECONNRESET') {
    console.log('Ignored ECONNRESET in Astro dev server.');
  } else {
    console.error(err);
    process.exit(1);
  }
});
