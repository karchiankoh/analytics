import Form from 'react-bootstrap/Form';

function Select( changeFunction ) {
  console.log(changeFunction)
  return (
    <Form.Select aria-label="Number of Tiers" onChange={changeFunction.changeFunction}>
      <option>Number of Tiers</option>
      {Array.from({ length: 10 }).map((_, index) => (
        <option key={index+1} value={index+1}>{index+1}</option>
      ))} 
    </Form.Select>
  );
}

export default Select;