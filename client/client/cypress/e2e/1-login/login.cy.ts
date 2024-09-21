describe("홈페이지 테스트", () => {
  it("인덱스페이지의 h1 태그에 '안녕' 메시지가 있는지 확인한다", () => {
    cy.visit("/");
    cy.get("[data-cy=testHeading]").should("have.text", "안녕");
  });
});
