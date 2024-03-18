// function getOffset(currentPage = 1, listPerPage) {
//   return (currentPage - 1) * [listPerPage];
// }

function emptyOrRows(res) {
  if (!res) {
    return [];
  }
  return res;
}

function mapToArray(rows) {
  const arr = [Object.keys(rows[0])];
  rows.forEach(r => arr.push(Object.values(r)));
  return arr;
}

function mapToTableData(rows) {
  const result = [];
  for (var i in rows)
    result.push([i, rows[i]]);
  return result;
}

module.exports = {
  // getOffset,
  emptyOrRows,
  mapToArray,
  mapToTableData
}