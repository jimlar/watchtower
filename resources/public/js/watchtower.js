
$(function() {
  $.get(document.location + 'jobs.json', function(data) {
    $('.result').html(data);
    alert('Load was performed: ' + data);
  });
});

