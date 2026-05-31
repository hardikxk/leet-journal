process.on('uncaughtException', (err) => {
  if (err.code === 'ECONNRESET') {
    console.log('Ignored ECONNRESET');
  } else {
    console.error(err);
    process.exit(1);
  }
});

require('child_process').execSync('npm run dev -- --port 4321', { stdio: 'inherit' });
